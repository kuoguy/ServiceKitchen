package clparker.servicekitchen;

import android.util.Log;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chrisp on 02/01/2017.
 */

public class CloudDBConnector {

    List<Order> getOrders(MobileServiceClient mClient, int location)
    {
        List<Order> orderList=null;
        try
        {
            orderList = mClient.getTable(Order.class).where().field("location").eq(location)
                    .execute().get();
        }
        catch(Exception e)
        {
            Log.d("CloudDBConnector", "getOrders failed: "+e.getMessage());
        }

        //Now sets lines of order- fetches from cloud
        List<Order_Line> orderLinesList=null;
        if(orderList!=null)
        {
            for (int count = 0; count < orderList.size(); count++)
            {
                orderList.get(count).setOnscreen(false);
                String orderId=orderList.get(count).getOrder_id();
                try {
                    orderLinesList = mClient.getTable(Order_Line.class).where().field("order").eq(orderId)
                            .execute().get();

                    orderList.get(count).setLines(orderLinesList);
                    Log.d("CloudDBConnector", "id: " + orderList.get(count).getId());
                }
                catch(Exception e)
                {
                    //orderLinesList=null;
                    Log.d("CloudDBConnector", "getOrderLines failed: "+e.getMessage());
                }
                List<Recipe> foundRecipe = null;
                if(orderLinesList!=null) {

                    for (int countLines = 0; countLines < orderLinesList.size(); countLines++) {
                        String recipeId = orderLinesList.get(countLines).getRecipe();
                        Log.d("CloudDBConnector", "orderline id: " + orderLinesList.get(countLines).getId() + " recipeid: " + recipeId);
                        try {

                            foundRecipe = mClient.getTable(Recipe.class).where().field("recipe_id").eq(recipeId)
                                    .execute().get();

                            orderLinesList.get(countLines).setLine(foundRecipe.get(0));

                        } catch (Exception e) {
                            foundRecipe = null;
                            Log.d("CloudDBConnector", "getRecipe for orderline failed: " + e.getMessage());
                        }

                        if(foundRecipe!=null)
                        {
                            List<Recipe_Line> foundRecipeLines=null;
                            try
                            {
                                foundRecipeLines = mClient.getTable(Recipe_Line.class).where().field("recipe_id").eq(recipeId).execute().get();
                                Log.d("CloudDBConnector", "Recipe Line id: " + foundRecipeLines.get(countLines).getId() + " recipelineid: " + recipeId);
                                foundRecipe.get(0).setLines(foundRecipeLines);



                            }
                            catch(Exception e)
                            {
                                foundRecipeLines=null;
                                Log.d("CloudDBConnector", "getRecipeLines for recipe failed: "+e.getMessage());
                            }

                            if(foundRecipeLines!=null)
                            {
                                List<Item> foundItem=null;
                                String itemId;
                                for(int lineCount=0; lineCount<foundRecipeLines.size(); lineCount++)
                                {
                                    itemId=foundRecipeLines.get(lineCount).getItem_id();
                                    try
                                    {
                                        foundItem = mClient.getTable(Item.class).where().field("id").eq(itemId)
                                                .execute().get();
                                        Log.d("CloudDBConnector", "item id: " + foundItem.get(0).getId()+ " name: "+foundItem.get(0).getItemName());
                                        foundRecipeLines.get(lineCount).setItem(foundItem.get(0));

                                    }
                                    catch(Exception e)
                                    {
                                        Log.d("CloudDBConnector", "getItems for recipeline failed: "+e.getMessage());
                                    }
                                }

                            }




                        }

                    }
                }


            }
        }

        return orderList;
    }

    public void changeOrderStatus(MobileServiceClient mClient, Order order)
    {
        try {
            mClient.getTable(Order.class).delete(order.getId());
        }
        catch(Exception e)
        {
        Log.d("CloudDBConnector", "setOrders failed: "+e.getMessage());
        }

        this.addOrder(mClient, order);
    }

    public ArrayList<Order> checkAndGetOrders(MobileServiceClient mClient, ArrayList<Order> currentOrders, int location)
    {
        boolean result=false;
        ArrayList<Order> difference=new ArrayList<>();
        List<Order> orderList=null;
        try
        {
            orderList = mClient.getTable(Order.class).where().field("location").eq(location)
                    .execute().get();
        }
        catch(Exception e)
        {
            Log.d("CloudDBConnector", "getOrders failed: "+e.getMessage());
        }

        if(orderList!=null)
        {
            if (orderList.size() > currentOrders.size())
                result = true;
        }

        if(result) {
            for (int count = currentOrders.size(); count < orderList.size(); count++) {
                difference.add(orderList.get(count));
            }

            List<Order_Line> orderLinesList = null;
            if (orderList != null) {
                for (int count = 0; count < difference.size(); count++) {
                    String orderId = difference.get(count).getOrder_id();
                    try {
                        orderLinesList = mClient.getTable(Order_Line.class).where().field("order").eq(orderId)
                                .execute().get();

                        difference.get(count).setLines(orderLinesList);
                        Log.d("CloudDBConnector", "id: " + orderList.get(count).getId());
                    } catch (Exception e) {
                        //orderLinesList=null;
                        Log.d("CloudDBConnector", "getOrderLines failed: " + e.getMessage());
                    }
                    List<Recipe> foundRecipe = null;
                    if (orderLinesList != null) {

                        for (int countLines = 0; countLines < orderLinesList.size(); countLines++) {
                            String recipeId = orderLinesList.get(countLines).getRecipe();
                            Log.d("CloudDBConnector", "orderline id: " + orderLinesList.get(countLines).getId() + " recipeid: " + recipeId);
                            try {

                                foundRecipe = mClient.getTable(Recipe.class).where().field("recipe_id").eq(recipeId)
                                        .execute().get();

                                orderLinesList.get(countLines).setLine(foundRecipe.get(0));

                            } catch (Exception e) {
                                foundRecipe = null;
                                Log.d("CloudDBConnector", "getRecipe for orderline failed: " + e.getMessage());
                            }

                            if (foundRecipe != null) {
                                List<Recipe_Line> foundRecipeLines = null;
                                try {
                                    foundRecipeLines = mClient.getTable(Recipe_Line.class).where().field("recipe_id").eq(recipeId).execute().get();
                                    Log.d("CloudDBConnector", "Recipe Line id: " + foundRecipeLines.get(countLines).getId() + " recipelineid: " + recipeId);
                                    foundRecipe.get(0).setLines(foundRecipeLines);


                                } catch (Exception e) {
                                    foundRecipeLines = null;
                                    Log.d("CloudDBConnector", "getRecipeLines for recipe failed: " + e.getMessage());
                                }

                                if (foundRecipeLines != null) {
                                    List<Item> foundItem = null;
                                    String itemId;
                                    for (int lineCount = 0; lineCount < foundRecipeLines.size(); lineCount++) {
                                        itemId = foundRecipeLines.get(lineCount).getItem_id();
                                        try {
                                            foundItem = mClient.getTable(Item.class).where().field("id").eq(itemId)
                                                    .execute().get();
                                            Log.d("CloudDBConnector", "item id: " + foundItem.get(0).getId() + " name: " + foundItem.get(0).getItemName());
                                            foundRecipeLines.get(lineCount).setItem(foundItem.get(0));

                                        } catch (Exception e) {
                                            Log.d("CloudDBConnector", "getItems for recipeline failed: " + e.getMessage());
                                        }
                                    }

                                }


                            }

                        }
                    }


                }

            }
        }


        return difference;
    }






    List<Category> getCategories(MobileServiceClient mClient)
    {
        List<Category> catList;
        try {
            catList = mClient.getTable(Category.class).execute().get();
        }
        catch(Exception e)
        {
            catList=null;
            Log.d("CloudDBConnector", "getCat failed: "+e.getMessage());
        }

        return catList;

    }

    List<SubCategory> getSubCategories(MobileServiceClient mClient)
    {
        List<SubCategory> subCatList;
        try {
            subCatList = mClient.getTable("sub_category", SubCategory.class).execute().get();
        }
        catch(Exception e)
        {
            subCatList=null;
            Log.d("CloudDBConnector", "getSubCat failed: "+e.getMessage());
        }

        return subCatList;

    }

    List<SubCategory> getSubCategoriesOfCategory(MobileServiceClient mClient, Category parent)
    {
        List<SubCategory> subCatList;
        try{
            subCatList = mClient.getTable("sub_category", SubCategory.class).where()
                    .field("categoryName").eq(parent.getCategoryName())
                    .execute().get();
            Log.d("CloudDBConnector", "cat= "+parent.getCategoryName());
        }
        catch(Exception e)
        {
            subCatList=null;
            Log.d("CloudDBConnector", "getSubCatofCat failed: "+e.getMessage());
        }
        return subCatList;
    }

    List<Item> getItems(MobileServiceClient mClient)
    {
        List<Item> itemList;
        try {
            itemList = mClient.getTable(Item.class).execute().get();
        }
        catch(Exception e)
        {
            itemList=null;
            Log.d("CloudDBConnector", "getItem failed: "+e.getMessage());
        }

        return itemList;

    }

    List<Item> getItemsOfSubCategory(MobileServiceClient mClient, SubCategory parent)
    {
        List<Item> itemList;
        try {
            itemList = mClient.getTable(Item.class).where()
                    .field("sub_category").eq(parent.getSubCategoryName())
                    .execute().get();
        }
        catch(Exception e)
        {
            itemList=null;
            Log.d("CloudDBConnector", "getItemofSubCat failed: "+e.getMessage());
        }

        return itemList;

    }

    public List<Amount> getMeasures(MobileServiceClient mClient)
    {
        List<Amount> measureList;
        try{
            measureList = mClient.getTable("measure", Amount.class).execute().get();
        }
        catch (Exception e)
        {
            measureList=null;
            Log.d("CloudDBConnector", "Get Measures List Failed: "+e.getMessage());
        }
        return measureList;
    }

    public List<Recipe> getRecipes(MobileServiceClient mClient)
    {
        List<Recipe> recipeList;
        try{
            recipeList = mClient.getTable(Recipe.class).execute().get();
        }
        catch(Exception e)
        {
            recipeList=null;
            Log.d("CloudDBConnector", "Get Recipe List failed: "+e.getMessage());
        }
        return recipeList;
    }

    public Recipe getSingleRecipe(MobileServiceClient mClient, String recipeName)
    {
        List<Recipe> toReturn;
        Recipe toReturnRecipe;
        try{
            toReturn = mClient.getTable(Recipe.class).where().field("name")
                    .eq(recipeName).execute().get();
            toReturnRecipe=toReturn.get(0);
        }
        catch(Exception e)
        {
            toReturnRecipe=null;
            Log.d("CloudDBConnector", "Get Recipe by name failed: "+e.getMessage());
        }

        return toReturnRecipe;
    }

    public List<Recipe_Line> getRecipeLines(MobileServiceClient mClient, Recipe recipe)
    {
        List<Recipe_Line> recipeLineList;
        try{
            recipeLineList = mClient.getTable(Recipe_Line.class).where().field("recipe_name")
                    .eq(recipe.getName()).execute().get();
        }
        catch(Exception e)
        {
            recipeLineList=null;
            Log.d("CloudDBConnector", "Get Recipe Line List failed: "+e.getMessage());
        }
        return recipeLineList;
    }


    //Add methods

    public void addOrder(MobileServiceClient mClient, Order newOrder)
    {
        //Log.d("DBCloud", "Adding order "+newOrder.getLocation()+" "+newOrder.getTable());
        MobileServiceJsonTable orderTable = mClient.getTable("order");
        JsonObject orderObject = new JsonObject();
        orderObject.addProperty("location", Integer.toString(newOrder.getLocation()));
        orderObject.addProperty("table", Integer.toString(newOrder.getTable()));
        orderObject.addProperty("createdTime", newOrder.getCreatedTime());
        orderObject.addProperty("status", newOrder.getStatus());
        orderObject.addProperty("order_id", newOrder.getOrder_id());

        try
        {
            orderTable.insert(orderObject).get();
        }
        catch(Exception exception)
        {
            if(mClient==null)
                Log.d("DBCloud", "Royally Fucked");
            Log.d("DBCloud", "Order "+exception.getMessage());
        }

        //ArrayList<Order_Line> lines = newOrder.getLines();

        //for(int count=0; count<lines.size(); count++)
        //{
        //    addOrderLine(mClient, lines.get(count));
        //}

    }

    public void addOrderLine(MobileServiceClient mClient, Order_Line newOrder)
    {
        MobileServiceJsonTable orderTable = mClient.getTable("order_line");
        JsonObject orderObject = new JsonObject();
        orderObject.addProperty("recipe", newOrder.getRecipe());
        orderObject.addProperty("order", newOrder.getOrderId());
        orderObject.addProperty("quantity", newOrder.getQuantity());
        try
        {
            orderTable.insert(orderObject).get();
        }
        catch(Exception exception)
        {
            Log.d("DBCloud", "orderline "+exception.getMessage());
        }
    }



}
