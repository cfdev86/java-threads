package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {

    private ProductInfoService productInfoService = new ProductInfoService();
    private ReviewService reviewService = new ReviewService();
    private InventoryService inventoryService = new InventoryService();

    private ProductServiceUsingCompletableFuture psscf = new ProductServiceUsingCompletableFuture(productInfoService, reviewService, inventoryService);

    @Test
    void retrieveProductDetails() {

        String productId = "ABC123";

        Product product = psscf.retrieveProductDetails(productId);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());

    }

    @Test
    void retrieveProductDetailsWithInventory() {

        String productId = "ABC123";

        Product product = psscf.retrieveProductDetailsWithInventory(productId);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        product.getProductInfo().getProductOptions()
                        .stream().forEach(productOption -> {
                            assertNotNull(productOption.getInventory());
                });
        assertNotNull(product.getReview());

    }


    @Test
    void retrieveProductDetailsWithInventory_approach2() {

        String productId = "ABC123";

        Product product = psscf.retrieveProductDetailsWithInventory_approach2(productId);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        product.getProductInfo().getProductOptions()
                .stream().forEach(productOption -> {
                    assertNotNull(productOption.getInventory());
                });
        assertNotNull(product.getReview());

    }

}