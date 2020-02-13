package org.nearbyshops.RESTEndpointReviewItem;

import org.nearbyshops.DAOReviewItem.FavoriteItemDAOPrepared;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ModelEndpointReview.FavouriteItemEndpoint;
import org.nearbyshops.Model.ModelReviewItem.FavouriteItem;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

/**
 * Created by sumeet on 9/8/16.
 */

@Path("/api/v1/FavouriteItem")
public class FavouriteItemResource {


    private FavoriteItemDAOPrepared favoriteItemDAOPrepared = Globals.favoriteItemDAOPrepared;


        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response saveFavouriteBook(FavouriteItem item)
        {
            int idOfInsertedRow = favoriteItemDAOPrepared.saveFavouriteItem(item);

            item.setItemID(idOfInsertedRow);

            if(idOfInsertedRow >=1)
            {
                Response response = Response.status(Response.Status.CREATED)
                        .location(URI.create("/api/v1/FavouriteItem/" + idOfInsertedRow))
                        .entity(item)
                        .build();

                return response;

            }else if(idOfInsertedRow <= 0)
            {
                Response response = Response.status(Response.Status.NOT_MODIFIED)
                        .entity(null)
                        .build();

                return response;
            }


            return null;
        }



        @DELETE
        @Produces(MediaType.APPLICATION_JSON)
        public Response deleteItem(@QueryParam("ItemID")Integer itemID,
                                   @QueryParam("EndUserID")Integer endUserID)
        {

            int rowCount = favoriteItemDAOPrepared.deleteFavouriteItem(itemID,endUserID);


            if(rowCount>=1)
            {
                Response response = Response.status(Response.Status.OK)
                        .entity(null)
                        .build();

                return response;
            }

            if(rowCount == 0)
            {
                Response response = Response.status(Response.Status.NOT_MODIFIED)
                        .entity(null)
                        .build();

                return response;
            }

            return null;
        }



        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response getFavouriteItems(
                @QueryParam("ItemID")Integer itemID,
                @QueryParam("EndUserID")Integer endUserID,
                @QueryParam("SortBy") String sortBy,
                @QueryParam("Limit")Integer limit, @QueryParam("Offset")Integer offset,
                @QueryParam("metadata_only")Boolean metaonly)
        {

            int set_limit = 30;
            int set_offset = 0;
            final int max_limit = 100;

            if(limit!= null)
            {

                if (limit >= max_limit) {

                    set_limit = max_limit;
                }
                else
                {

                    set_limit = limit;
                }

            }

            if(offset!=null)
            {
                set_offset = offset;
            }

            FavouriteItemEndpoint endPoint = favoriteItemDAOPrepared.getEndPointMetadata(itemID,endUserID);

            endPoint.setLimit(set_limit);
            endPoint.setMax_limit(max_limit);
            endPoint.setOffset(set_offset);

            List<FavouriteItem> list = null;


            if(metaonly==null || (!metaonly)) {

                list =
                        favoriteItemDAOPrepared.getFavouriteItem(
                                itemID,endUserID,
                                sortBy,set_limit,set_offset
                        );

                endPoint.setResults(list);
            }


            //Marker
            return Response.status(Response.Status.OK)
                    .entity(endPoint)
                    .build();

        }

}
