package com.github.thyago.ifood.restaurant;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurant")
public class RestaurantResource {

    @GET
    public List<Restaurant> list() {
        return Restaurant.listAll();
    }

    @POST
    @Transactional
    public void create(Restaurant dto) {
        dto.persist();
        Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void update(@PathParam("id") Long id, Restaurant dto) {
        Optional<Restaurant> restautanteOp = Restaurant.findByIdOptional(id);
        if (restautanteOp.isEmpty()) {
            throw new NotFoundException();
        }
        Restaurant restaurant = restautanteOp.get();
        restaurant.name = dto.name;
        restaurant.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void remove(@PathParam("id") Long id) {
        Optional<Restaurant> restautanteOp = Restaurant.findByIdOptional(id);
        restautanteOp.ifPresentOrElse(Restaurant::delete, () -> { throw new NotFoundException(); });
    }

    // DISHES

    @GET
    @Path("{restaurantId}/dishes")
    @Tag(name = "dish")
    public List<Dish> dishList(@PathParam("restaurantId") Long restaurantId) {
        Optional<Restaurant> restaurantOp = Restaurant.findByIdOptional(restaurantId);
        if (restaurantOp.isEmpty()) {
            throw new NotFoundException();
        }
        return Dish.list("restaurant", restaurantOp.get());
    }

    @POST
    @Path("{restaurantId}/dishes")
    @Tag(name = "dish")
    @Transactional
    public void dishCreate(@PathParam("restaurantId") Long restaurantId, Dish dto) {
        Optional<Restaurant> restaurantOp = Restaurant.findByIdOptional(restaurantId);
        if (restaurantOp.isEmpty()) {
            throw new NotFoundException();
        }
        Dish dish = new Dish();
        dish.restaurant = restaurantOp.get();
        dish.name = dto.name;
        dish.description = dto.description;
        dish.price = dto.price;
        dish.persist();
        Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{restaurantId}/dishes/{id}")
    @Tag(name = "dish")
    @Transactional
    public void dishUpdate(@PathParam("restaurantId") Long restaurantId, @PathParam("id") Long id, Dish dto) {
        Optional<Dish> dishOp = Dish.findByIdOptional(id);
        if (dishOp.isEmpty() || !dishOp.get().restaurant.id.equals(restaurantId)) {
            throw new NotFoundException();
        }
        Dish dish = dishOp.get();
        dish.name = dto.name;
        dish.description = dto.description;
        dish.price = dto.price;
        dish.persist();
    }

    @DELETE
    @Path("{restaurantId}/dishes/{id}")
    @Tag(name = "dish")
    @Transactional
    public void dishRemove(@PathParam("restaurantId") Long restaurantId, @PathParam("id") Long id) {
        Optional<Dish> dishOp = Dish.findByIdOptional(id);
        if (dishOp.isEmpty() || !dishOp.get().restaurant.id.equals(restaurantId)) {
            throw new NotFoundException();
        }
        dishOp.get().delete();
    }
}
