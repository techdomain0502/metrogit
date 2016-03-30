package com.delhi.metro.sasha.route;

/**
 * Created by sachin on 3/28/2016.
 */
public class Pair{
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (s != null ? !s.equals(pair.s) : pair.s != null) return false;
        return !(d != null ? !d.equals(pair.d) : pair.d != null);

    }

    @Override
    public int hashCode() {
        int result = s != null ? s.hashCode() : 0;
        result = 31 * result + (d != null ? d.hashCode() : 0);
        return result;
    }

    private final Vertex s;
    private final Vertex d;
    public Pair(Vertex s,Vertex d){
        this.s = s;
        this.d = d;
    }
}
