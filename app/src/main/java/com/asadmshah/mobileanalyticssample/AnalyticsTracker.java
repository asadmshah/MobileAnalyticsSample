package com.asadmshah.mobileanalyticssample;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

/**
 * Created by asadmshah on 7/3/15.
 */
public class AnalyticsTracker {

    private final Tracker mTracker;

    private static final String EVENT_CATEGORY_SHOPPING_CART_ACTIONS = "Shopping Cart Actions";
    private static final String EVENT_CATEGORY_PRODUCT_ADDED_TO_CART = "Product Added to Cart";
    private static final String EVENT_CATEGORY_PRODUCT_REMOVED_FROM_CART = "Product Removed from Cart";
    private static final String EVENT_CATEGORY_PRODUCT_DETAIL_VIEWED = "Product Detail Viewed";
    private static final String EVENT_CATEGORY_PRODUCT_CHECKOUT_STARTED = "Product Checkout Started";
    private static final String EVENT_CATEGORY_PRODUCT_PAYMENT_INFORMATION_SUBMITTED = "Product Payment Information Submitted";
    private static final String EVENT_CATEGORY_PRODUCT_ORDER_REVIEWED = "Product Order Reviewed";
    private static final String EVENT_CATEGORY_PRODUCT_PURCHASE_COMPLETED = "Product Purchase Completed";

    public AnalyticsTracker(Tracker tracker) {
        mTracker = tracker;
    }

    public void sendClearCartHit() {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(EVENT_CATEGORY_SHOPPING_CART_ACTIONS)
                .setAction("Clear Cart")
                .build());
    }

    public void sendProductAddedToCartHit(Phone phone) {
        Product product = buildProduct(phone);
        ProductAction action = new ProductAction(ProductAction.ACTION_ADD);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(EVENT_CATEGORY_PRODUCT_ADDED_TO_CART)
                .setLabel(phone.name)
                .addProduct(product)
                .setProductAction(action)
                .build());
    }

    public void sendProductRemovedFromCartHit(Phone phone) {
        Product product = buildProduct(phone);
        ProductAction action = new ProductAction(ProductAction.ACTION_REMOVE);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(EVENT_CATEGORY_PRODUCT_REMOVED_FROM_CART)
                .setLabel(phone.name)
                .addProduct(product)
                .setProductAction(action)
                .build());
    }

    public void sendCheckoutStartedHit(ShoppingCart shoppingCart) {
        ProductAction action = new ProductAction(ProductAction.ACTION_CHECKOUT);
        action.setTransactionId(shoppingCart.getTransactionId());
        String category = EVENT_CATEGORY_PRODUCT_CHECKOUT_STARTED;
        HitBuilders.EventBuilder eventBuilder = buildEventForShoppingCart(shoppingCart, action, category);
        mTracker.send(eventBuilder.build());

        action = new ProductAction(ProductAction.ACTION_CHECKOUT_OPTION);
        action.setTransactionId(shoppingCart.getTransactionId());
        action.setCheckoutStep(1);
        eventBuilder = buildEventForShoppingCart(shoppingCart, action, category);
        mTracker.send(eventBuilder.build());
    }

    public void sendPaymentInformationSubmittedHit(ShoppingCart shoppingCart) {
        ProductAction action = new ProductAction(ProductAction.ACTION_CHECKOUT_OPTION);
        action.setTransactionId(shoppingCart.getTransactionId());
        action.setCheckoutStep(2);
        String category = EVENT_CATEGORY_PRODUCT_PAYMENT_INFORMATION_SUBMITTED;
        HitBuilders.EventBuilder eventBuilder = buildEventForShoppingCart(shoppingCart, action, category);
        mTracker.send(eventBuilder.build());
    }

    public void sendOrderReviewedHit(ShoppingCart shoppingCart) {
        ProductAction action = new ProductAction(ProductAction.ACTION_CHECKOUT_OPTION);
        action.setTransactionId(shoppingCart.getTransactionId());
        action.setCheckoutStep(3);
        String category = EVENT_CATEGORY_PRODUCT_ORDER_REVIEWED;
        HitBuilders.EventBuilder eventBuilder = buildEventForShoppingCart(shoppingCart, action, category);
        mTracker.send(eventBuilder.build());
    }

    public void sendPurchaseCompletedHit(ShoppingCart shoppingCart) {
        ProductAction action = new ProductAction(ProductAction.ACTION_PURCHASE);
        action.setTransactionId(shoppingCart.getTransactionId());
        String category = EVENT_CATEGORY_PRODUCT_PURCHASE_COMPLETED;
        HitBuilders.EventBuilder eventBuilder = buildEventForShoppingCart(shoppingCart, action, category);
        mTracker.send(eventBuilder.build());
    }

    private HitBuilders.EventBuilder buildEventForShoppingCart(ShoppingCart shoppingCart, ProductAction action, String category) {
        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                .setCategory(category)
                .setProductAction(action);
        for (Phone phone : shoppingCart) {
            eventBuilder.addProduct(buildProduct(phone));
        }
        return eventBuilder;
    }

    public void sendProductDetailViewedHit(Phone phone) {
        Product product = buildProduct(phone);
        ProductAction action = new ProductAction(ProductAction.ACTION_DETAIL);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(EVENT_CATEGORY_PRODUCT_DETAIL_VIEWED)
                .setLabel(phone.name)
                .addProduct(product)
                .setProductAction(action)
                .build());
    }

    private Product buildProduct(Phone phone) {
        return new Product()
                .setName(phone.name)
                .setPrice(phone.price)
                .setId(phone.id)
                .setQuantity(1);
    }

}
