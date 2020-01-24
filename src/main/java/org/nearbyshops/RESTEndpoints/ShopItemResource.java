package org.nearbyshops.RESTEndpoints;

import org.nearbyshops.DAOsPrepared.ItemCategoryDAO;
import org.nearbyshops.DAOsPrepared.ShopItemByItemDAO;
import org.nearbyshops.DAOsPrepared.ShopItemByShopDAO;
import org.nearbyshops.DAOsPrepared.ShopItemDAO;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ShopItem;
import org.nearbyshops.ModelEndpoint.ShopItemEndPoint;
import org.nearbyshops.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.ModelRoles.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;


@Path("/api/v1/ShopItem")
public class ShopItemResource {



	private ShopItemByShopDAO shopItemByShopDAO = Globals.shopItemByShopDAO;
	private ShopItemByItemDAO shopItemByItemDAO = Globals.shopItemByItemDAO;
	private ShopItemDAO shopItemDAO = Globals.shopItemDAO;
	private ItemCategoryDAO itemCategoryDAO = Globals.itemCategoryDAO;



	@PUT
	@Path("/UpdateBulk")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN, GlobalConstants.ROLE_SHOP_STAFF})
	public Response updateShopItemBulk(List<ShopItem> itemList)
	{


		int rowCountSum = 0;
		int shopID = 0;


		User shopAdmin = (User) Globals.accountApproved;


		if(shopAdmin.getRole()==GlobalConstants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = Globals.daoUserUtility.getShopIDForShopAdmin(shopAdmin.getUserID());
		}
		else if(shopAdmin.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE)
		{

			shopID = Globals.daoUserUtility.getShopIDforShopStaff(shopAdmin.getUserID());
			ShopStaffPermissions permissions = Globals.daoShopStaff.getShopStaffPermissions(shopAdmin.getUserID());



			if(!permissions.isPermitUpdateItemsInShop())
			{
				// staff member do not have this permission
				throw new ForbiddenException("Not Permitted !");
			}

		}




		for(ShopItem shopItem : itemList)
		{
			shopItem.setShopID(shopID);
			rowCountSum = rowCountSum + shopItemDAO.updateShopItem(shopItem);
		}





		if(rowCountSum ==  itemList.size())
		{

			return Response.status(Status.OK)
					.entity(null)
					.build();
		}
		else if( rowCountSum < itemList.size() && rowCountSum > 0)
		{

			return Response.status(Status.PARTIAL_CONTENT)
					.entity(null)
					.build();
		}
		else if(rowCountSum == 0 ) {

			return Response.status(Status.NOT_MODIFIED)
					.entity(null)
					.build();
		}

		return null;
	}





	@POST
	@Path("/CreateBulk")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN, GlobalConstants.ROLE_SHOP_STAFF})
	public Response createShopItemBulk(List<ShopItem> itemList)
	{
		int rowCountSum = 0;



		User user = (User) Globals.accountApproved;

		if(user.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE) {

			int shopID = Globals.daoUserUtility.getShopIDforShopStaff(user.getUserID());
			ShopStaffPermissions permissions = Globals.daoShopStaff.getShopStaffPermissions(user.getUserID());


			if (!permissions.isPermitAddRemoveItems()) {
				// staff member do not have this permission
				throw new ForbiddenException("Not Permitted !");
			}



			for(ShopItem shopItem : itemList)
			{
				shopItem.setShopID(shopID);
				rowCountSum = rowCountSum + shopItemDAO.insertShopItem(shopItem);
			}


		}
		else if(user.getRole()==GlobalConstants.ROLE_SHOP_ADMIN_CODE)
		{
			int shopID = Globals.daoUserUtility.getShopIDForShopAdmin(user.getUserID());


			for(ShopItem shopItem : itemList)
			{
				shopItem.setShopID(shopID);
				rowCountSum = rowCountSum + shopItemDAO.insertShopItem(shopItem);
			}
		}




		if(rowCountSum ==  itemList.size())
		{

			return Response.status(Status.OK)
					.build();
		}
		else if( (rowCountSum < itemList.size()) && (rowCountSum > 0))
		{

			return Response.status(Status.PARTIAL_CONTENT)
					.build();
		}
		else if(rowCountSum == 0 ) {

			return Response.status(Status.NOT_MODIFIED)
					.build();
		}

		return null;
	}







	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
	public Response createShopItem(ShopItem shopItem)
	{
		int rowCount = 0;
		int shopID = 0;

		User user = (User) Globals.accountApproved;


		if(user.getRole()==GlobalConstants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = Globals.daoUserUtility.getShopIDForShopAdmin(user.getUserID());
		}
		else if (user.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE)
		{

			shopID = Globals.daoUserUtility.getShopIDforShopStaff(user.getUserID());
			ShopStaffPermissions permissions = Globals.daoShopStaff.getShopStaffPermissions(user.getUserID());


			if (!permissions.isPermitUpdateItemsInShop()) {
				// staff member do not have this permission
				throw new ForbiddenException("Not Permitted !");
			}
		}




		shopItem.setShopID(shopID);
		rowCount = shopItemDAO.insertShopItem(shopItem);



		if(rowCount == 1)
		{

			return Response.status(Status.CREATED)
								.build();
			
		}else if(rowCount <= 0)
		{

			return Response.status(Status.NOT_MODIFIED)
					.build();
		}


		return null;
	}






	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN,GlobalConstants.ROLE_SHOP_STAFF})
	public Response updateShopItem(ShopItem shopItem)
	{

//		System.out.println("Inside Resource Method !");

		int rowCount = 0;

		int shopID = 0;


		User user = (User) Globals.accountApproved;


		if(user.getRole()==GlobalConstants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = Globals.daoUserUtility.getShopIDForShopAdmin(user.getUserID());
		}
		else if (user.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE)
		{

			shopID = Globals.daoUserUtility.getShopIDforShopStaff(user.getUserID());
			ShopStaffPermissions permissions = Globals.daoShopStaff.getShopStaffPermissions(user.getUserID());


			if (!permissions.isPermitUpdateItemsInShop()) {
				// staff member do not have this permission
				throw new ForbiddenException("Not Permitted !");
			}
		}



		shopItem.setShopID(shopID);
		rowCount = shopItemDAO.updateShopItem(shopItem);

		
		if(rowCount == 1)
		{

			return Response.status(Status.OK)
								.build();
			
		}else if(rowCount <= 0)
		{

			return Response.status(Status.NOT_MODIFIED)
					.build();
		}

		return null;
	}




	@DELETE
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN,GlobalConstants.ROLE_SHOP_STAFF})
	public Response deleteShopItem(@QueryParam("ShopID")int ShopID, @QueryParam("ItemID") int itemID)
	{
		int rowCount = 0;
		int shopID = 0;

		User user = (User) Globals.accountApproved;


		if(user.getRole()==GlobalConstants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = Globals.daoUserUtility.getShopIDForShopAdmin(user.getUserID());
		}
		else if (user.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE)
		{

			shopID = Globals.daoUserUtility.getShopIDforShopStaff(user.getUserID());
			ShopStaffPermissions permissions = Globals.daoShopStaff.getShopStaffPermissions(user.getUserID());

			if (!permissions.isPermitUpdateItemsInShop()) {
				// staff member do not have this permission
				throw new ForbiddenException("Not Permitted !");
			}
		}





		rowCount =	shopItemDAO.deleteShopItem(shopID, itemID);

		
		if(rowCount == 1)
		{

			return Response.status(Status.OK)
					.build();
			
		}else if(rowCount <= 0)
		{

			return Response.status(Status.NOT_MODIFIED)
					.build();
		}
	
		return null;
	}





	@POST
	@Path("/DeleteBulk")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN, GlobalConstants.ROLE_SHOP_STAFF})
	public Response deleteShopItemBulk(List<ShopItem> itemList)
	{
		int rowCountSum = 0;

		User user = (User) Globals.accountApproved;

		int shopID = 0;

		if(user.getRole()==GlobalConstants.ROLE_SHOP_ADMIN_CODE)
		{
			 shopID = Globals.daoUserUtility.getShopIDForShopAdmin(user.getUserID());

		}
		else if (user.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE)
		{

			ShopStaffPermissions permissions = Globals.daoShopStaff.getShopStaffPermissions(user.getUserID());


			if(!permissions.isPermitAddRemoveItems())
			{
				// staff member do not have permission
				throw new ForbiddenException("Not Permitted !");
			}


			shopID = Globals.daoUserUtility.getShopIDforShopStaff(user.getUserID());
		}




		for(ShopItem shopItem : itemList)
		{
			shopItem.setShopID(shopID);

			rowCountSum = rowCountSum + shopItemDAO
					.deleteShopItem(shopItem.getShopID(),shopItem.getItemID());
		}



		if(rowCountSum ==  itemList.size())
		{

			return Response.status(Status.OK)
					.entity(null)
					.build();
		}
		else if( rowCountSum < itemList.size() && rowCountSum > 0)
		{

			return Response.status(Status.PARTIAL_CONTENT)
					.build();
		}
		else if(rowCountSum == 0 ) {

			return Response.status(Status.NOT_MODIFIED)
					.build();
		}

		return null;
	}






	@GET
	@Path("/ForShop")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShopItemsForShop(
            @QueryParam("ItemCategoryID")Integer ItemCategoryID,
            @QueryParam("ShopID")Integer ShopID, @QueryParam("ItemID") Integer itemID,
            @QueryParam("SearchString") String searchString,
            @QueryParam("SortBy") String sortBy,
            @QueryParam("Limit") Integer limit, @QueryParam("Offset")Integer offset
	)
	{



		ShopItemEndPoint endPoint = shopItemDAO.getShopItemsForShop(
				ItemCategoryID,ShopID,itemID,
				searchString,
				sortBy,limit,offset
		);


		endPoint.setLimit(limit);
//		endPoint.setMax_limit(max_limit);
		endPoint.setOffset(offset);


		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

		//Marker
		return Response.status(Status.OK)
				.entity(endPoint)
				.build();
	}






	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShopItems(
            @QueryParam("ItemCategoryID")Integer ItemCategoryID,
			@QueryParam("GetSubcategories")boolean getSubcategories,
            @QueryParam("ShopID")Integer ShopID, @QueryParam("ItemID") Integer itemID,
            @QueryParam("latCenter")Double latCenter, @QueryParam("lonCenter")Double lonCenter,
            @QueryParam("deliveryRangeMax")Double deliveryRangeMax,
            @QueryParam("deliveryRangeMin")Double deliveryRangeMin,
            @QueryParam("proximity")Double proximity,
            @QueryParam("EndUserID") Integer endUserID, @QueryParam("IsFilledCart") Boolean isFilledCart,
            @QueryParam("IsOutOfStock") Boolean isOutOfStock, @QueryParam("PriceEqualsZero")Boolean priceEqualsZero,
            @QueryParam("MinPrice")Integer minPrice, @QueryParam("MaxPrice")Integer maxPrice,
            @QueryParam("SearchString") String searchString,
            @QueryParam("ShopEnabled")Boolean shopEnabled,
            @QueryParam("SortBy") String sortBy,
            @QueryParam("Limit") Integer limit, @QueryParam("Offset") int offset,
            @QueryParam("metadata_only")Boolean metaonly,
            @QueryParam("GetExtras")Boolean getExtras
	)
	{

		final int max_limit = 100;

		if(limit!=null)
		{
			if(limit>=max_limit)
			{
				limit = max_limit;
			}
		}
		else
		{
			limit = 30;
		}


		ShopItemEndPoint endPoint;


		if(getExtras!=null && getExtras)
		{

			if(ShopID!=null && itemID == null)
			{
				endPoint = shopItemByShopDAO.getEndpointMetadata(
						ItemCategoryID,
						ShopID,
						latCenter,lonCenter,
						deliveryRangeMin,deliveryRangeMax,
						proximity,endUserID,
						isFilledCart,isOutOfStock,
						priceEqualsZero, searchString,
						shopEnabled
				);
			}
			else if(itemID !=null && ShopID==null)
			{
				endPoint = shopItemByItemDAO.getEndpointMetadata(
						ItemCategoryID,
						itemID,
						latCenter,lonCenter,
						deliveryRangeMin,deliveryRangeMax,
						proximity,endUserID,
						isFilledCart,isOutOfStock,priceEqualsZero
				);

			}
			else
			{
				endPoint = shopItemDAO.getEndpointMetadata(
						ItemCategoryID,
						ShopID,itemID,
						latCenter,lonCenter,
						deliveryRangeMin,deliveryRangeMax,
						proximity,endUserID,
						isFilledCart,isOutOfStock,priceEqualsZero,
						searchString);
			}

		}
		else
		{
			endPoint = shopItemDAO.getEndpointMetadata(
					ItemCategoryID,
					ShopID,itemID,
					latCenter,lonCenter,
					deliveryRangeMin,deliveryRangeMax,
					proximity,endUserID,
					isFilledCart,isOutOfStock,priceEqualsZero,
					searchString);
		}


		endPoint.setLimit(limit);
		endPoint.setMax_limit(max_limit);
		endPoint.setOffset(offset);


		ArrayList<ShopItem> shopItemsList = null;


		if(metaonly==null || (!metaonly)) {



			if(getExtras!=null && getExtras)
			{

				if(ShopID!=null && itemID == null)
				{

					shopItemsList = shopItemByShopDAO.getShopItems(
							ItemCategoryID,
							ShopID,
							latCenter, lonCenter,
							deliveryRangeMin,deliveryRangeMax,
							proximity, endUserID,
							isFilledCart,
							isOutOfStock,
							priceEqualsZero,
							shopEnabled,
							searchString,
							sortBy,limit,offset
					);
				}
				else if(itemID !=null && ShopID==null)
				{

					shopItemsList = shopItemByItemDAO.getShopItems(
							ItemCategoryID,
							itemID,
							latCenter, lonCenter,
							deliveryRangeMin,deliveryRangeMax,
							proximity, endUserID,
							isFilledCart,
							isOutOfStock,
							priceEqualsZero,
							sortBy,
							limit,offset
					);

				}
				else
				{

					shopItemsList = shopItemDAO.getShopItems(
							ItemCategoryID,
							ShopID, itemID,
							latCenter, lonCenter,
							deliveryRangeMin,deliveryRangeMax,
							proximity, endUserID,
							isFilledCart,
							isOutOfStock,
							priceEqualsZero,
							searchString,
							sortBy,
							limit,offset);

				}

			}
			else
			{
				shopItemsList = shopItemDAO.getShopItems(
						ItemCategoryID,
						ShopID, itemID,
						latCenter, lonCenter,
						deliveryRangeMin,deliveryRangeMax,
						proximity, endUserID,
						isFilledCart,
						isOutOfStock,
						priceEqualsZero,
						searchString,
						sortBy,
						limit,offset);

			}

			endPoint.setResults(shopItemsList);
		}

//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}






		List<ItemCategory> subcategories;

		if(getSubcategories)
		{
			subcategories = itemCategoryDAO.getItemCategoriesJoinRecursive(
					ShopID, ItemCategoryID, null,
					latCenter, lonCenter,
					deliveryRangeMin,
					deliveryRangeMax,
					proximity,
					true,
					searchString,
					ItemCategory.CATEGORY_ORDER,
					null,null
			);



			endPoint.setSubcategories(subcategories);
		}




		//Marker
		return Response.status(Status.OK)
				.entity(endPoint)
				.build();
	}
	
}
