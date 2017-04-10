package clparker.servicekitchen;

/**
 * Created by Clown on 02/01/2017.
 */

public class Order_Line {

    private String id;
    private String order;
    private Recipe line;
    private String recipe;
    private int quantity=0;

    public void setLine(Recipe nLine){this.line=nLine;}
    public void setQuantity(int nQuantity){this.quantity=nQuantity;}
    public void setOrderId(String nOrderId){this.order=nOrderId;}
    public void setId(String nId){this.id=nId;}
    public void setRecipe(String nId){this.recipe=nId;}
    public Recipe getLine(){return this.line;}
    public int getQuantity(){return this.quantity;}
    public String getOrderId(){return this.order;}
    public String getId(){return this.id;}
    public String getRecipe(){return this.recipe;}

}
