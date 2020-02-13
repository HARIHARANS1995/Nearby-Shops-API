package org.nearbyshops.RESTEndpointsCart;

import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ModelDelivery.DeliveryAddress;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.List;





@Path("/api/DeliveryAddress")
public class DeliveryAddressResource {




	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAddress(DeliveryAddress address)
	{

		int idOfInsertedRow = Globals.deliveryAddressService.saveAddress(address);

		address.setId(idOfInsertedRow);


		if(idOfInsertedRow >=1)
		{


			return Response.status(Status.CREATED)
					.location(URI.create("/api/DeliveryAddress/" + idOfInsertedRow))
					.entity(address)
					.build();
			
		}
		else
			{

			//Response.status(Status.CREATED).location(arg0)
			return Response.status(Status.NOT_MODIFIED)
					.build();
		}

	}





	
	@PUT
	@Path("/{DeliveryAddressID}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCart(@PathParam("DeliveryAddressID")int addressID, DeliveryAddress deliveryAddress)
	{

		deliveryAddress.setId(addressID);

		int rowCount = Globals.deliveryAddressService.updateAddress(deliveryAddress);


		if(rowCount >= 1)
		{

			return Response.status(Status.OK)
					.build();
		}
		if(rowCount == 0)
		{

			return Response.status(Status.NOT_MODIFIED)
					.build();
		}


		return null;
	}





	@DELETE
	@Path("/{id}")
	public Response deleteCart(@PathParam("id")int addressID)
	{

		int rowCount = Globals.deliveryAddressService.deleteAddress(addressID);
		
		
		if(rowCount>=1)
		{

			return Response.status(Status.OK)
					.build();
		}
		else if(rowCount == 0)
		{
			return Response.status(Status.NOT_MODIFIED)
					.build();
		}
		
		
		return null;
	}



	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDeliveryAddresses(@QueryParam("EndUserID")int endUserID)
	{

		//List<Cart> cartList = Globals.cartService.readCarts(endUserID,shopID);
		List<DeliveryAddress> addressesList = Globals.deliveryAddressService.readAddresses(endUserID);

		GenericEntity<List<DeliveryAddress>> listEntity = new GenericEntity<List<DeliveryAddress>>(addressesList){
			
		};

		
		if(addressesList.size()<=0)
		{
			return Response.status(Status.NO_CONTENT)
					.build();
		}
		else
		{
			return Response.status(Status.OK)
					.entity(listEntity)
					.build();
		}
		
	}



	
	@GET
	@Path("/{DeliveryAddressID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDeliveryAddress(@PathParam("DeliveryAddressID")int addressID)
	{

		DeliveryAddress deliveryAddress = Globals.deliveryAddressService.readAddress(addressID);
		
		if(deliveryAddress != null)
		{

			return Response.status(Status.OK)
					.entity(deliveryAddress)
					.build();
			
		} else 
		{

			return Response.status(Status.NO_CONTENT)
					.build();
			
		}
		
	}


}
