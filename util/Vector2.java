package util;

public class Vector2 {
    public final float x, y;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "X: " + this.x + " Y: " + this.y;
    }

    public Vector2 add(Vector2 v2){
        float newx = this.x + v2.x;
        float newy = this.y + v2.y;
        Vector2 result = new Vector2(newx, newy);
        return result;
    }

    public Vector2 subtract(Vector2 v2){
        float newx = this.x - v2.x;
        float newy = this.y - v2.y;
        Vector2 result = new Vector2(newx, newy);
        return result;
    }

    public Vector2 scalar(float s){
        float newx = this.x * s;
        float newy = this.y * s;
        Vector2 result = new Vector2(newx, newy);
        return result;
    }

    public Vector2 normalize(){
        float mag = this.magnitude();
        // prevent divide-by-zero vector, though this is not necessarily mathematically correct.
        if(mag == 0){
            return new Vector2(0, 0);
        }
        return new Vector2(this.x / mag, this.y / mag);
    }

    public float dot(Vector2 v2){
        float result = 0;
        result += this.x * v2.x;
        result += this.y * v2.y;
        return result;
    }

    public float distance(Vector2 v2){
        float result = (float)Math.sqrt(Math.pow(this.x - v2.x, 2) + Math.pow(this.y - v2.y, 2));
        return result;
    }

    public float distanceManhattan(Vector2 v2){
        return Math.abs(this.x - v2.x) + Math.abs(this.y - v2.y); 
    }

    public float worstCaseDistance(Vector2 v2){
        return Math.abs(this.x - v2.x) * Math.abs(this.y - v2.y); 
    }

    public Vector2 absSubtract(Vector2 v2){
        return new Vector2(Math.abs(this.x - v2.x), Math.abs(this.y - v2.y));
    }

    public float magnitude(){
        return (float)Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector2 scalarMax(float max){
        if((this.x <= max) && (this.y <= max)){
            return this;
        }
        return new Vector2(Math.max(this.x, max), Math.max(this.y, max));
    }
}
