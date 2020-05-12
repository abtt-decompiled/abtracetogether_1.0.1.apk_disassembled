package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = 1;
    /* access modifiers changed from: private */
    public final transient ConcurrentMap<E, AtomicInteger> countMap;

    private class EntrySet extends EntrySet {
        private EntrySet() {
            super();
        }

        /* access modifiers changed from: 0000 */
        public ConcurrentHashMultiset<E> multiset() {
            return ConcurrentHashMultiset.this;
        }

        public Object[] toArray() {
            return snapshot().toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return snapshot().toArray(tArr);
        }

        private List<Entry<E>> snapshot() {
            ArrayList newArrayListWithExpectedSize = Lists.newArrayListWithExpectedSize(size());
            Iterators.addAll(newArrayListWithExpectedSize, iterator());
            return newArrayListWithExpectedSize;
        }
    }

    private static class FieldSettersHolder {
        static final FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");

        private FieldSettersHolder() {
        }
    }

    public /* bridge */ /* synthetic */ boolean contains(@NullableDecl Object obj) {
        return super.contains(obj);
    }

    public /* bridge */ /* synthetic */ Set elementSet() {
        return super.elementSet();
    }

    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    public static <E> ConcurrentHashMultiset<E> create() {
        return new ConcurrentHashMultiset<>(new ConcurrentHashMap());
    }

    public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> iterable) {
        ConcurrentHashMultiset<E> create = create();
        Iterables.addAll(create, iterable);
        return create;
    }

    public static <E> ConcurrentHashMultiset<E> create(ConcurrentMap<E, AtomicInteger> concurrentMap) {
        return new ConcurrentHashMultiset<>(concurrentMap);
    }

    ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> concurrentMap) {
        Preconditions.checkArgument(concurrentMap.isEmpty(), "the backing map (%s) must be empty", (Object) concurrentMap);
        this.countMap = concurrentMap;
    }

    public int count(@NullableDecl Object obj) {
        AtomicInteger atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, obj);
        if (atomicInteger == null) {
            return 0;
        }
        return atomicInteger.get();
    }

    public int size() {
        long j = 0;
        for (AtomicInteger atomicInteger : this.countMap.values()) {
            j += (long) atomicInteger.get();
        }
        return Ints.saturatedCast(j);
    }

    public Object[] toArray() {
        return snapshot().toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return snapshot().toArray(tArr);
    }

    private List<E> snapshot() {
        ArrayList newArrayListWithExpectedSize = Lists.newArrayListWithExpectedSize(size());
        for (Entry entry : entrySet()) {
            Object element = entry.getElement();
            for (int count = entry.getCount(); count > 0; count--) {
                newArrayListWithExpectedSize.add(element);
            }
        }
        return newArrayListWithExpectedSize;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005a, code lost:
        r2 = new java.util.concurrent.atomic.AtomicInteger(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0065, code lost:
        if (r4.countMap.putIfAbsent(r5, r2) == null) goto L_0x006f;
     */
    public int add(E e, int i) {
        AtomicInteger atomicInteger;
        AtomicInteger atomicInteger2;
        Preconditions.checkNotNull(e);
        if (i == 0) {
            return count(e);
        }
        CollectPreconditions.checkPositive(i, "occurences");
        do {
            atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, e);
            if (atomicInteger == null) {
                atomicInteger = (AtomicInteger) this.countMap.putIfAbsent(e, new AtomicInteger(i));
                if (atomicInteger == null) {
                    return 0;
                }
            }
            while (true) {
                int i2 = atomicInteger.get();
                if (i2 == 0) {
                    break;
                }
                try {
                    if (atomicInteger.compareAndSet(i2, IntMath.checkedAdd(i2, i))) {
                        return i2;
                    }
                } catch (ArithmeticException unused) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Overflow adding ");
                    sb.append(i);
                    sb.append(" occurrences to a count of ");
                    sb.append(i2);
                    throw new IllegalArgumentException(sb.toString());
                }
            }
        } while (!this.countMap.replace(e, atomicInteger, atomicInteger2));
        return 0;
    }

    public int remove(@NullableDecl Object obj, int i) {
        int i2;
        int max;
        if (i == 0) {
            return count(obj);
        }
        CollectPreconditions.checkPositive(i, "occurences");
        AtomicInteger atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, obj);
        if (atomicInteger == null) {
            return 0;
        }
        do {
            i2 = atomicInteger.get();
            if (i2 == 0) {
                return 0;
            }
            max = Math.max(0, i2 - i);
        } while (!atomicInteger.compareAndSet(i2, max));
        if (max == 0) {
            this.countMap.remove(obj, atomicInteger);
        }
        return i2;
    }

    public boolean removeExactly(@NullableDecl Object obj, int i) {
        int i2;
        int i3;
        if (i == 0) {
            return true;
        }
        CollectPreconditions.checkPositive(i, "occurences");
        AtomicInteger atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, obj);
        if (atomicInteger == null) {
            return false;
        }
        do {
            i2 = atomicInteger.get();
            if (i2 < i) {
                return false;
            }
            i3 = i2 - i;
        } while (!atomicInteger.compareAndSet(i2, i3));
        if (i3 == 0) {
            this.countMap.remove(obj, atomicInteger);
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002c, code lost:
        if (r6 != 0) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002e, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002f, code lost:
        r2 = new java.util.concurrent.atomic.AtomicInteger(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003a, code lost:
        if (r4.countMap.putIfAbsent(r5, r2) == null) goto L_0x0044;
     */
    public int setCount(E e, int i) {
        AtomicInteger atomicInteger;
        AtomicInteger atomicInteger2;
        Preconditions.checkNotNull(e);
        CollectPreconditions.checkNonnegative(i, "count");
        do {
            atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, e);
            if (atomicInteger == null) {
                if (i == 0) {
                    return 0;
                }
                atomicInteger = (AtomicInteger) this.countMap.putIfAbsent(e, new AtomicInteger(i));
                if (atomicInteger == null) {
                    return 0;
                }
            }
            while (true) {
                int i2 = atomicInteger.get();
                if (i2 == 0) {
                    break;
                } else if (atomicInteger.compareAndSet(i2, i)) {
                    if (i == 0) {
                        this.countMap.remove(e, atomicInteger);
                    }
                    return i2;
                }
            }
        } while (!this.countMap.replace(e, atomicInteger, atomicInteger2));
        return 0;
    }

    public boolean setCount(E e, int i, int i2) {
        Preconditions.checkNotNull(e);
        CollectPreconditions.checkNonnegative(i, "oldCount");
        CollectPreconditions.checkNonnegative(i2, "newCount");
        AtomicInteger atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, e);
        boolean z = false;
        if (atomicInteger != null) {
            int i3 = atomicInteger.get();
            if (i3 == i) {
                if (i3 == 0) {
                    if (i2 == 0) {
                        this.countMap.remove(e, atomicInteger);
                        return true;
                    }
                    AtomicInteger atomicInteger2 = new AtomicInteger(i2);
                    if (this.countMap.putIfAbsent(e, atomicInteger2) == null || this.countMap.replace(e, atomicInteger, atomicInteger2)) {
                        z = true;
                    }
                    return z;
                } else if (atomicInteger.compareAndSet(i3, i2)) {
                    if (i2 == 0) {
                        this.countMap.remove(e, atomicInteger);
                    }
                    return true;
                }
            }
            return false;
        } else if (i != 0) {
            return false;
        } else {
            if (i2 == 0) {
                return true;
            }
            if (this.countMap.putIfAbsent(e, new AtomicInteger(i2)) == null) {
                z = true;
            }
            return z;
        }
    }

    /* access modifiers changed from: 0000 */
    public Set<E> createElementSet() {
        final Set keySet = this.countMap.keySet();
        return new ForwardingSet<E>() {
            /* access modifiers changed from: protected */
            public Set<E> delegate() {
                return keySet;
            }

            public boolean contains(@NullableDecl Object obj) {
                return obj != null && Collections2.safeContains(keySet, obj);
            }

            public boolean containsAll(Collection<?> collection) {
                return standardContainsAll(collection);
            }

            public boolean remove(Object obj) {
                return obj != null && Collections2.safeRemove(keySet, obj);
            }

            public boolean removeAll(Collection<?> collection) {
                return standardRemoveAll(collection);
            }
        };
    }

    /* access modifiers changed from: 0000 */
    public Iterator<E> elementIterator() {
        throw new AssertionError("should never be called");
    }

    @Deprecated
    public Set<Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    /* access modifiers changed from: 0000 */
    public int distinctElements() {
        return this.countMap.size();
    }

    public boolean isEmpty() {
        return this.countMap.isEmpty();
    }

    /* access modifiers changed from: 0000 */
    public Iterator<Entry<E>> entryIterator() {
        final AnonymousClass2 r0 = new AbstractIterator<Entry<E>>() {
            private final Iterator<Map.Entry<E, AtomicInteger>> mapEntries = ConcurrentHashMultiset.this.countMap.entrySet().iterator();

            /* access modifiers changed from: protected */
            public Entry<E> computeNext() {
                while (this.mapEntries.hasNext()) {
                    Map.Entry entry = (Map.Entry) this.mapEntries.next();
                    int i = ((AtomicInteger) entry.getValue()).get();
                    if (i != 0) {
                        return Multisets.immutableEntry(entry.getKey(), i);
                    }
                }
                return (Entry) endOfData();
            }
        };
        return new ForwardingIterator<Entry<E>>() {
            @NullableDecl
            private Entry<E> last;

            /* access modifiers changed from: protected */
            public Iterator<Entry<E>> delegate() {
                return r0;
            }

            public Entry<E> next() {
                Entry<E> entry = (Entry) super.next();
                this.last = entry;
                return entry;
            }

            public void remove() {
                CollectPreconditions.checkRemove(this.last != null);
                ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
                this.last = null;
            }
        };
    }

    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    public void clear() {
        this.countMap.clear();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.countMap);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, (Object) (ConcurrentMap) objectInputStream.readObject());
    }
}
