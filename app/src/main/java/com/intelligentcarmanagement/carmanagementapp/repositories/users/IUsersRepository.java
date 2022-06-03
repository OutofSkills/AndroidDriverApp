package com.intelligentcarmanagement.carmanagementapp.repositories.users;

import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IGetUserResponse;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IAvailableResponse;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IUpdateLocation;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IUpdateUserResponse;
import com.intelligentcarmanagement.carmanagementapp.models.User;

public interface IUsersRepository {

    void getUserByEmail(String email, IGetUserResponse getUserResponse);

    void updateUser(int id, User user, IUpdateUserResponse updateUserResponse);

    void makeAvailable(int id, boolean isAvailable, IAvailableResponse makeAvailableResponse);

    void isAvailable(String token, int id, IAvailableResponse isAvailableResponse);

    void updateLocation(String token, int id, String latitude, String longitude, IUpdateLocation updateLocationResponse);
}
