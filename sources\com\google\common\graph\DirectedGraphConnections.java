package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.graph.ElementOrder.Type;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

final class DirectedGraphConnections<N, V> implements GraphConnections<N, V> {
    private static final Object PRED = new Object();
    /* access modifiers changed from: private */
    public final Map<N, Object> adjacentNodeValues;
    /* access modifiers changed from: private */
    @NullableDecl
    public final List<NodeConnection<N>> orderedNodeConnections;
    /* access modifiers changed from: private */
    public int predecessorCount;
    /* access modifiers changed from: private */
    public int successorCount;

    /* renamed from: com.google.common.graph.DirectedGraphConnections$8 reason: invalid class name */
    static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$graph$ElementOrder$Type;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$com$google$common$graph$ElementOrder$Type = iArr;
            iArr[Type.UNORDERED.ordinal()] = 1;
            $SwitchMap$com$google$common$graph$ElementOrder$Type[Type.STABLE.ordinal()] = 2;
        }
    }

    private static abstract class NodeConnection<N> {
        final N node;

        static final class Pred<N> extends NodeConnection<N> {
            Pred(N n) {
                super(n);
            }

            public boolean equals(Object obj) {
                if (obj instanceof Pred) {
                    return this.node.equals(((Pred) obj).node);
                }
                return false;
            }

            public int hashCode() {
                return Pred.class.hashCode() + this.node.hashCode();
            }
        }

        static final class Succ<N> extends NodeConnection<N> {
            Succ(N n) {
                super(n);
            }

            public boolean equals(Object obj) {
                if (obj instanceof Succ) {
                    return this.node.equals(((Succ) obj).node);
                }
                return false;
            }

            public int hashCode() {
                return Succ.class.hashCode() + this.node.hashCode();
            }
        }

        NodeConnection(N n) {
            this.node = Preconditions.checkNotNull(n);
        }
    }

    private static final class PredAndSucc {
        /* access modifiers changed from: private */
        public final Object successorValue;

        PredAndSucc(Object obj) {
            this.successorValue = obj;
        }
    }

    private DirectedGraphConnections(Map<N, Object> map, @NullableDecl List<NodeConnection<N>> list, int i, int i2) {
        this.adjacentNodeValues = (Map) Preconditions.checkNotNull(map);
        this.orderedNodeConnections = list;
        this.predecessorCount = Graphs.checkNonNegative(i);
        this.successorCount = Graphs.checkNonNegative(i2);
        Preconditions.checkState(i <= map.size() && i2 <= map.size());
    }

    static <N, V> DirectedGraphConnections<N, V> of(ElementOrder<N> elementOrder) {
        ArrayList arrayList;
        int i = AnonymousClass8.$SwitchMap$com$google$common$graph$ElementOrder$Type[elementOrder.type().ordinal()];
        if (i == 1) {
            arrayList = null;
        } else if (i == 2) {
            arrayList = new ArrayList();
        } else {
            throw new AssertionError(elementOrder.type());
        }
        return new DirectedGraphConnections<>(new HashMap(4, 1.0f), arrayList, 0, 0);
    }

    static <N, V> DirectedGraphConnections<N, V> ofImmutable(N n, Iterable<EndpointPair<N>> iterable, Function<N, V> function) {
        HashMap hashMap = new HashMap();
        Builder builder = ImmutableList.builder();
        int i = 0;
        int i2 = 0;
        for (EndpointPair endpointPair : iterable) {
            if (endpointPair.nodeU().equals(n) && endpointPair.nodeV().equals(n)) {
                hashMap.put(n, new PredAndSucc(function.apply(n)));
                builder.add((Object) new Pred(n));
                builder.add((Object) new Succ(n));
                i++;
            } else if (endpointPair.nodeV().equals(n)) {
                Object nodeU = endpointPair.nodeU();
                Object put = hashMap.put(nodeU, PRED);
                if (put != null) {
                    hashMap.put(nodeU, new PredAndSucc(put));
                }
                builder.add((Object) new Pred(nodeU));
                i++;
            } else {
                Preconditions.checkArgument(endpointPair.nodeU().equals(n));
                Object nodeV = endpointPair.nodeV();
                Object apply = function.apply(nodeV);
                Object put2 = hashMap.put(nodeV, apply);
                if (put2 != null) {
                    Preconditions.checkArgument(put2 == PRED);
                    hashMap.put(nodeV, new PredAndSucc(apply));
                }
                builder.add((Object) new Succ(nodeV));
            }
            i2++;
        }
        return new DirectedGraphConnections<>(hashMap, builder.build(), i, i2);
    }

    public Set<N> adjacentNodes() {
        if (this.orderedNodeConnections == null) {
            return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
        }
        return new AbstractSet<N>() {
            public UnmodifiableIterator<N> iterator() {
                final Iterator it = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                final HashSet hashSet = new HashSet();
                return new AbstractIterator<N>() {
                    /* access modifiers changed from: protected */
                    public N computeNext() {
                        while (it.hasNext()) {
                            NodeConnection nodeConnection = (NodeConnection) it.next();
                            if (hashSet.add(nodeConnection.node)) {
                                return nodeConnection.node;
                            }
                        }
                        return endOfData();
                    }
                };
            }

            public int size() {
                return DirectedGraphConnections.this.adjacentNodeValues.size();
            }

            public boolean contains(@NullableDecl Object obj) {
                return DirectedGraphConnections.this.adjacentNodeValues.containsKey(obj);
            }
        };
    }

    public Set<N> predecessors() {
        return new AbstractSet<N>() {
            public UnmodifiableIterator<N> iterator() {
                if (DirectedGraphConnections.this.orderedNodeConnections == null) {
                    final Iterator it = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                    return new AbstractIterator<N>() {
                        /* access modifiers changed from: protected */
                        public N computeNext() {
                            while (it.hasNext()) {
                                Entry entry = (Entry) it.next();
                                if (DirectedGraphConnections.isPredecessor(entry.getValue())) {
                                    return entry.getKey();
                                }
                            }
                            return endOfData();
                        }
                    };
                }
                final Iterator it2 = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                return new AbstractIterator<N>() {
                    /* access modifiers changed from: protected */
                    public N computeNext() {
                        while (it2.hasNext()) {
                            NodeConnection nodeConnection = (NodeConnection) it2.next();
                            if (nodeConnection instanceof Pred) {
                                return nodeConnection.node;
                            }
                        }
                        return endOfData();
                    }
                };
            }

            public int size() {
                return DirectedGraphConnections.this.predecessorCount;
            }

            public boolean contains(@NullableDecl Object obj) {
                return DirectedGraphConnections.isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }

    public Set<N> successors() {
        return new AbstractSet<N>() {
            public UnmodifiableIterator<N> iterator() {
                if (DirectedGraphConnections.this.orderedNodeConnections == null) {
                    final Iterator it = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                    return new AbstractIterator<N>() {
                        /* access modifiers changed from: protected */
                        public N computeNext() {
                            while (it.hasNext()) {
                                Entry entry = (Entry) it.next();
                                if (DirectedGraphConnections.isSuccessor(entry.getValue())) {
                                    return entry.getKey();
                                }
                            }
                            return endOfData();
                        }
                    };
                }
                final Iterator it2 = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                return new AbstractIterator<N>() {
                    /* access modifiers changed from: protected */
                    public N computeNext() {
                        while (it2.hasNext()) {
                            NodeConnection nodeConnection = (NodeConnection) it2.next();
                            if (nodeConnection instanceof Succ) {
                                return nodeConnection.node;
                            }
                        }
                        return endOfData();
                    }
                };
            }

            public int size() {
                return DirectedGraphConnections.this.successorCount;
            }

            public boolean contains(@NullableDecl Object obj) {
                return DirectedGraphConnections.isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }

    public Iterator<EndpointPair<N>> incidentEdgeIterator(final N n) {
        final Iterator it;
        List<NodeConnection<N>> list = this.orderedNodeConnections;
        if (list == null) {
            it = Iterators.concat(Iterators.transform(predecessors().iterator(), new Function<N, EndpointPair<N>>() {
                public EndpointPair<N> apply(N n) {
                    return EndpointPair.ordered(n, n);
                }
            }), Iterators.transform(successors().iterator(), new Function<N, EndpointPair<N>>() {
                public EndpointPair<N> apply(N n) {
                    return EndpointPair.ordered(n, n);
                }
            }));
        } else {
            it = Iterators.transform(list.iterator(), new Function<NodeConnection<N>, EndpointPair<N>>() {
                public EndpointPair<N> apply(NodeConnection<N> nodeConnection) {
                    if (nodeConnection instanceof Succ) {
                        return EndpointPair.ordered(n, nodeConnection.node);
                    }
                    return EndpointPair.ordered(nodeConnection.node, n);
                }
            });
        }
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        return new AbstractIterator<EndpointPair<N>>() {
            /* access modifiers changed from: protected */
            public EndpointPair<N> computeNext() {
                while (it.hasNext()) {
                    EndpointPair<N> endpointPair = (EndpointPair) it.next();
                    if (endpointPair.nodeU().equals(endpointPair.nodeV())) {
                        if (!atomicBoolean.getAndSet(true)) {
                        }
                    }
                    return endpointPair;
                }
                return (EndpointPair) endOfData();
            }
        };
    }

    public V value(N n) {
        V v = this.adjacentNodeValues.get(n);
        if (v == PRED) {
            return null;
        }
        if (v instanceof PredAndSucc) {
            v = ((PredAndSucc) v).successorValue;
        }
        return v;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0025  */
    public void removePredecessor(N n) {
        boolean z;
        Object obj = this.adjacentNodeValues.get(n);
        if (obj == PRED) {
            this.adjacentNodeValues.remove(n);
        } else if (obj instanceof PredAndSucc) {
            this.adjacentNodeValues.put(n, ((PredAndSucc) obj).successorValue);
        } else {
            z = false;
            if (!z) {
                int i = this.predecessorCount - 1;
                this.predecessorCount = i;
                Graphs.checkNonNegative(i);
                List<NodeConnection<N>> list = this.orderedNodeConnections;
                if (list != null) {
                    list.remove(new Pred(n));
                    return;
                }
                return;
            }
            return;
        }
        z = true;
        if (!z) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0026  */
    public V removeSuccessor(Object obj) {
        V v = this.adjacentNodeValues.get(obj);
        if (v != null) {
            V v2 = PRED;
            if (v != v2) {
                if (v instanceof PredAndSucc) {
                    this.adjacentNodeValues.put(obj, v2);
                    v = ((PredAndSucc) v).successorValue;
                } else {
                    this.adjacentNodeValues.remove(obj);
                }
                if (v != null) {
                    int i = this.successorCount - 1;
                    this.successorCount = i;
                    Graphs.checkNonNegative(i);
                    List<NodeConnection<N>> list = this.orderedNodeConnections;
                    if (list != null) {
                        list.remove(new Succ(obj));
                    }
                }
                return v;
            }
        }
        v = null;
        if (v != null) {
        }
        return v;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    public void addPredecessor(N n, V v) {
        Object put = this.adjacentNodeValues.put(n, PRED);
        boolean z = false;
        if (put != null) {
            if (put instanceof PredAndSucc) {
                this.adjacentNodeValues.put(n, put);
            } else if (put != PRED) {
                this.adjacentNodeValues.put(n, new PredAndSucc(put));
            }
            if (!z) {
                int i = this.predecessorCount + 1;
                this.predecessorCount = i;
                Graphs.checkPositive(i);
                List<NodeConnection<N>> list = this.orderedNodeConnections;
                if (list != null) {
                    list.add(new Pred(n));
                    return;
                }
                return;
            }
            return;
        }
        z = true;
        if (!z) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0031  */
    public V addSuccessor(N n, V v) {
        V put = this.adjacentNodeValues.put(n, v);
        if (put != null) {
            if (put instanceof PredAndSucc) {
                this.adjacentNodeValues.put(n, new PredAndSucc(v));
                put = ((PredAndSucc) put).successorValue;
            } else if (put == PRED) {
                this.adjacentNodeValues.put(n, new PredAndSucc(v));
            }
            if (put == null) {
                int i = this.successorCount + 1;
                this.successorCount = i;
                Graphs.checkPositive(i);
                List<NodeConnection<N>> list = this.orderedNodeConnections;
                if (list != null) {
                    list.add(new Succ(n));
                }
            }
            return put;
        }
        put = null;
        if (put == null) {
        }
        return put;
    }

    /* access modifiers changed from: private */
    public static boolean isPredecessor(@NullableDecl Object obj) {
        return obj == PRED || (obj instanceof PredAndSucc);
    }

    /* access modifiers changed from: private */
    public static boolean isSuccessor(@NullableDecl Object obj) {
        return (obj == PRED || obj == null) ? false : true;
    }
}
