package struct;

public class PLUSMINUS implements MathObject{
    @Override
    public MathObject evaluate() throws Exception {
        this.getHeader();
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int compareTo(MathObject obj) {
        return 0;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public Symbol getHeader() {
        return null;
    }

    @Override
    public int getHierarchy() {
        return 0;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public boolean isSameSolution(MathObject obj) {
        return false;
    }
}
