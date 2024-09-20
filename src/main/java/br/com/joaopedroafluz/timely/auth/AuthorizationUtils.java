package br.com.joaopedroafluz.timely.auth;

import br.com.joaopedroafluz.timely.trip.Trip;
import br.com.joaopedroafluz.timely.tripParticipant.TripParticipant;
import br.com.joaopedroafluz.timely.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class AuthorizationUtils {

    public static final String ACCESS_DENIED_MESSAGE = "Você não possui permissão para realizar esta ação";

    @Setter
    @Getter
    private static User authenticatedUser;


    public static void throwExceptionIfUserDoesNotHavePermission(Trip trip) {
        var tripMembersCode = getTripMembersCode(trip);

        throwExceptionIfUserDoesNotHavePermission(tripMembersCode);
    }

    public static void throwExceptionIfUserDoesNotHavePermission(UUID validUserCode) {
        if (!validUserCode.equals(authenticatedUser.getCode())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    private static void throwExceptionIfUserDoesNotHavePermission(List<UUID> validUsersCode) {
        if (!validUsersCode.contains(authenticatedUser.getCode())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    private static ArrayList<UUID> getTripMembersCode(Trip trip) {
        var tripMembersCode = new ArrayList<>(List.of(trip.getOwner().getCode()));
        trip.getParticipants()
                .stream()
                .map(TripParticipant::getUser)
                .map(User::getCode)
                .forEach(tripMembersCode::add);

        return tripMembersCode;
    }

}
