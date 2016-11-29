package action;

import java.util.HashMap;

import messages.CreateCustomerMessage;
import messages.GetClientTokenMessage;
import messages.GetListingImagesMessage;
import messages.LenderMessage;
import messages.ListingAvailabilityMessage;
import messages.ListingImageMessage;
import messages.ListingMessage;
import messages.ListingReviewMessage;
import messages.MerchantAccountMessage;
import messages.Message;
import messages.ProfilePicMessage;
import messages.RegisterGCMMessage;
import messages.ReservationMessage;
import messages.SearchMessage;
import messages.SeekerFavoriteMessage;
import messages.SeekerMessage;
import messages.UserMessage;
import messages.ViewLenderMessage;

public class ActionFactory {
	
	private static HashMap<Class, Action> factory;
	
	public ActionFactory(){
		if (factory == null){
			factory = new HashMap<>();
			factory.put(SearchMessage.class, new SearchAction());
			factory.put(CreateCustomerMessage.class, new CreateCustomerAction());
			factory.put(GetClientTokenMessage.class, new GetClientTokenAction());
			factory.put(LenderMessage.class, new LenderAction());
			factory.put(ListingMessage.class, new ListingAction());
			factory.put(ListingReviewMessage.class, new ListingReviewAction());
			factory.put(MerchantAccountMessage.class, new MerchantAccountAction());
			factory.put(ProfilePicMessage.class, new ProfilePicAction());
			factory.put(ReservationMessage.class, new ReservationAction());
			factory.put(SeekerFavoriteMessage.class, new SeekerFavoriteAction());
			factory.put(SeekerMessage.class, new SeekerAction());
			factory.put(UserMessage.class, new UserAction());
			factory.put(ListingAvailabilityMessage.class, new ListingAvailabilityAction());
			factory.put(ListingImageMessage.class, new ListingImageAction());
			factory.put(GetListingImagesMessage.class, new GetListingImagesAction());
			factory.put(RegisterGCMMessage.class, new RegisterGCMAction());
			factory.put(ViewLenderMessage.class, new ViewLenderAction());
		}
	}
	
	public Action getAction(Message message){
		return factory.get(message.getClass());
	}

}
