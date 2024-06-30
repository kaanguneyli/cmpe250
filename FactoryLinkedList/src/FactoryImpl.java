import java.util.ArrayList;
import java.util.NoSuchElementException;

public class FactoryImpl implements Factory {
    private Holder first;
    private Holder last;
    private int size = 0;

    //ilkinde yaptığın değişikliklerden yola çıkarak diğer hepsini gözden geçir
    //en başta first last tanımlamak mantıklı olabilir
    //tüm methodları tek tek caselerine göre dene
    //reference'a göre çalışıyor, incelememiz lazım
    @Override
    public void addFirst(Product product) {
        if (size == 0) {
            first = new Holder(null, product, null);
            last = first;
        } else {
            Holder holder = new Holder(null, product, first);
            first.setPreviousHolder(holder);
            first = holder;
        }
        size++;
    }

    @Override
    public void addLast(Product product) {
        if (size == 0) {
            last = new Holder(null, product, null);
            first = last;
        } else {
            Holder holder = new Holder(last, product, null);
            last.setNextHolder(holder);
            last = holder;
        }
        size++;
    }

    @Override
    public Product removeFirst() throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException("Factory is empty.\n");
        } else if (size == 1) {
            Holder removed = first;
            first = null;
            last = null;
            size--;
            return removed.getProduct();
        } else {
            Holder removed = first;
            first = first.getNextHolder();
            first.setPreviousHolder(null);
            size--;
            return removed.getProduct();
        }
    }

    @Override
    public Product removeLast() throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException("Factory is empty.\n");
        } else if (size == 1) {
            Holder removed = last;
            first = null;
            last = null;
            size--;
            return removed.getProduct();
        } else {
            Holder removed = last;
            last = last.getPreviousHolder();
            last.setNextHolder(null);
            size--;
            return removed.getProduct();
        }
    }

    @Override
    public Product find(int id) throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException("Product not found.\n");
        } else {
            Holder current = first;
            Product res = null;
            while (current != null) {
                int currentId = current.getProduct().getId();
                if (id == currentId) {
                    res = current.getProduct();
                    break;
                } else {
                    current = current.getNextHolder();
                }
            }
            if (res != null)
                return res;
            else
                throw new NoSuchElementException("Product not found.\n");
        }
    }

    @Override
    public Product update(int id, Integer value) throws NoSuchElementException {
        Product product = find(id);
        Product prev = new Product(id, product.getValue());
        product.setValue(value);
        return prev; //EXCEPTION'INI KONTROL ET
    }

    @Override
    public Product get(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds.\n");
        } else if (index <= size / 2) {
            Holder holder = first;
            for (int i = 0; i < index; i++) {
                holder = holder.getNextHolder();
            }
            return holder.getProduct();
        } else {
            Holder holder = last;
            for (int i = size - 1; i > index; i--) {
                holder = holder.getPreviousHolder();
            }
            return holder.getProduct();
        }
    }

    @Override
    public void add(int index, Product product) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds.\n");
        } else if (index == 0) {
            addFirst(product);
        } else if (index == size) {
            addLast(product);
        } else if (index <= size / 2) {
            Holder holder = first;
            for (int i = 0; i < index - 1; i++) {
                holder = holder.getNextHolder();
            }
            Holder added = new Holder(holder, product, holder.getNextHolder());
            added.getPreviousHolder().setNextHolder(added);
            added.getNextHolder().setPreviousHolder(added);
            size++;
        } else {
            Holder holder = last;
            for (int i = size - 1; i > index; i--) {
                holder = holder.getPreviousHolder();
            }
            Holder added = new Holder(holder.getPreviousHolder(), product, holder);
            added.getPreviousHolder().setNextHolder(added);
            added.getNextHolder().setPreviousHolder(added);
            size++;
        }
    }

    @Override
    public Product removeIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.\n");
        } else {
            if (index == 0) {
                return removeFirst();
            } else if (index == size - 1) {
                return removeLast();
            } else if (index <= size / 2) {
                Holder holder = first;
                for (int i = 0; i < index; i++) {
                    holder = holder.getNextHolder();
                }
                size--;
                holder.getPreviousHolder().setNextHolder(holder.getNextHolder());
                holder.getNextHolder().setPreviousHolder(holder.getPreviousHolder());
                return holder.getProduct();
            } else {
                Holder holder = last;
                for (int i = size - 1; i > index; i--) {
                    holder = holder.getPreviousHolder();
                }
                size--;
                holder.getPreviousHolder().setNextHolder(holder.getNextHolder());
                holder.getNextHolder().setPreviousHolder(holder.getPreviousHolder());
                return holder.getProduct();
            }
        }
    }

    @Override
    public Product removeProduct(int value) throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException("Product not found.\n");
        } else {
            Product res = null;
            for (Holder current = first; current != null; current = current.getNextHolder()) {
                if (current.getProduct().getValue() == value) {
                    res = current.getProduct();
                    Holder prevHolder = current.getPreviousHolder();
                    Holder nextHolder = current.getNextHolder();
                    if (prevHolder == null) {
                        removeFirst();
                    } else if (nextHolder == null) {
                        removeLast();
                    } else {
                        prevHolder.setNextHolder(nextHolder);
                        nextHolder.setPreviousHolder(prevHolder);
                        size--;
                    }
                    break;
                }
            }
            if (res != null) {
                return res;
            } else {
                throw new NoSuchElementException("Product not found.\n");
            }
        }
    }

    @Override
    public int filterDuplicates() {
        ArrayList<Integer> uniques = new ArrayList<>();
        Holder currentHolder = first;
        int count = 0;
        for (int i = 0; i < size; i++) {
            int currentValue = currentHolder.getProduct().getValue();
            if (!uniques.contains(currentValue)) {
                uniques.add(currentValue);
            } else {
                removeIndex(i);
                count++;
                i--;
            }
            currentHolder = currentHolder.getNextHolder();
        }
        return count;
    }

    @Override
    public void reverse() {
        if (size > 1) {
            Holder temp;
            Holder current = first;
            for (; current != null; current = current.getPreviousHolder()) {
                temp = current.getPreviousHolder();
                current.setPreviousHolder(current.getNextHolder());
                current.setNextHolder(temp);
            }
            Holder temp2 = first;
            first = last;
            last = temp2;
        }
    }

    @Override
    public String toString() {
        String ret = "{";
        for (Holder h = first; h != null; h = h.getNextHolder()) {
            Product product = h.getProduct();
            if (h != last) {
                ret += "(" + product.getId() + ", " + product.getValue() + "),";
            } else {
                ret += "(" + product.getId() + ", " + product.getValue() + ")";
            }
        }
        ret += "}";
        return ret;
    }
}


