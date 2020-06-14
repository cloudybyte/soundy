package net.cloudybyte.bot.core.premiumcheck;

import com.mongodb.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import net.cloudybyte.bot.core.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class PremiumTierChecker {


    public int getPremiumTier(long discordidlong, long guildidlong) {

        //parse long values to string
        String discordid = String.valueOf(discordidlong);
        String guildid = String.valueOf(guildidlong);


        //set stripe api key
        Stripe.apiKey = Constants.StripeAPIkey;

        HashMap<String, Enum> customerCache = PremiumTierCache.customerCache;

        System.out.println("customerCache.get(discordid) = " + customerCache.get(discordid));

        //Connect to DB
        MongoClient mongoClient = null;
        mongoClient = new MongoClient(new MongoClientURI(Constants.DBUri));
        System.out.println("Constants.DBUri = " + Constants.DBUri);
        DB database = mongoClient.getDB("soundy");

        DBCollection collection = database.getCollection("customerDB");
        DBCollection collection2 = database.getCollection("volumes");


        //search for user in db
        DBObject query = new BasicDBObject("discordID", discordid);
        System.out.println("discordid = " + discordid);
        BasicDBObject select = new BasicDBObject();
        select.put("customerID", 1);
        DBCursor cursor = collection.find(query, select);
        try {
            //get customerID out of db
            System.out.println("collection.toString() = " + collection.toString());
            System.out.println("cursor = " + cursor);
            BasicDBObject obj = (BasicDBObject) cursor.next();
            String customerID = obj.getString("customerID");
            System.out.println("customerID = " + customerID);

            //retrieve customer from stripe
            Customer customer = Customer.retrieve(customerID);
            Map<String, String> customerMetadata = customer.getMetadata();
            System.out.println("customerMetadata.size() = " + customerMetadata.size());
            System.out.println("customerMetadata.toString() = " + customerMetadata.toString());
            String StripeDiscordID = customerMetadata.get("discordid");
          /* if (!customerMetadata.containsKey(discordid)) {

                //-2 means customer has no metadata
                return -2;
            } else {*/
                //Security Check
                if (!StripeDiscordID.equals(discordid)) {
                    //-3 means security check error (userid stored in stripe does not equal the actual userid)
                    return -3;
                }

                SubscriptionCollection customersubs = customer.getSubscriptions();
                List<Subscription> subscriptionList = null;
                subscriptionList = customersubs.getData();
            System.out.println("subscriptionList.size() = " + subscriptionList.size());
            if (subscriptionList.size() > 1) {
                    //-4 means more than one subsription
                    return -4;
                }
                String planID = subscriptionList.get(0).getPlan().getId();
                if (planID.equals(Constants.soundyPremiumID)) {
                    return 1;
                }
                if (planID.equals(Constants.soundyGoldID)) {
                    return 2;
                }

           // }
        } catch (NoSuchElementException ignored) {
            //-1 means no customer in db
            return -1;
        } catch (StripeException e) {
            //triggered when no customer exists
            //0 means to premium subscription
            return 0;
        }

    //-5 means method ran without a response
        return -5;
}

}
