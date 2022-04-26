package lsfusion.base.col.implementations.stored;

import lsfusion.base.col.implementations.abs.AMSet;
import lsfusion.base.col.interfaces.immutable.ImOrderSet;
import lsfusion.base.col.interfaces.immutable.ImRevMap;
import lsfusion.base.col.interfaces.immutable.ImSet;
import lsfusion.base.col.interfaces.mutable.mapvalue.ImRevValueMap;
import lsfusion.base.col.interfaces.mutable.mapvalue.ImValueMap;

public class StoredArIndexedSet<K> extends AMSet<K> {

    private StoredArray<K> array;
    
    public StoredArIndexedSet(StoredArray<K> array) {
        this.array = array;
    }

    public StoredArIndexedSet(K[] array, StoredArraySerializer serializer) throws StoredArray.StoredArrayCreationException {
        this(array.length, array, serializer);
    }
    
    public StoredArIndexedSet(int size, K[] array, StoredArraySerializer serializer) throws StoredArray.StoredArrayCreationException {
        this.array = new StoredArray<>(size, array, serializer, null);
    }
    
    public StoredArIndexedSet(int size, StoredArraySerializer serializer) {
        array = new StoredArray<>(size, serializer);
    }

    public StoredArIndexedSet(StoredArIndexedSet<K> set) {
        array = new StoredArray<>(set.array);
    }

    public int size() {
        return array.size();
    }

    public K get(int i) {
        return array.get(i);
    }

    public <M> ImValueMap<K, M> mapItValues() {
        return new StoredArIndexedMap<>(this);
    }

    public <M> ImRevValueMap<K, M> mapItRevValues() {
        return new StoredArIndexedMap<>(this);
    }

    @Override
    public boolean contains(K element) {
        return StoredArIndexedMap.findIndex(element, array) >= 0;
    }

    @Override
    public K getIdentIncl(K element) {
        return get(StoredArIndexedMap.findIndex(element, array));
    }

    @Override
    public void keep(K element) {
        assert size() == 0 || array.get(size() - 1).hashCode() <= element.hashCode();
        array.append(element);
    }

    public boolean add(K element) {
        throw new UnsupportedOperationException();
    }

    public ImSet<K> immutable() {
        return this;
    }

    public ImSet<K> immutableCopy() {
        return new StoredArIndexedSet<>(this);
    }

    @Override    
    public StoredArIndexedMap<K, K> toMap() {
        return new StoredArIndexedMap<>(array, array);
    }

    public ImRevMap<K, K> toRevMap() {
        return toMap();
    }

    public ImOrderSet<K> toOrderSet() {
        return new StoredArOrderIndexedSet<>(this, null);
    }

    public StoredArray<K> getStoredArray() {
        return array;
    }
}