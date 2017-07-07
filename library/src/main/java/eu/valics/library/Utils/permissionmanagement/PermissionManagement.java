package eu.valics.library.Utils.permissionmanagement;

/**
 * Created by L on 7/7/2017.
 */

public interface PermissionManagement {

    void onPermissionGranted(int requestCode); // user gave permission to app

    void onPermissionNotGranted(int requestCode); // user left to give permission, but didnt gone through

    void onPermissionAccepted(int requestCode); // user agreed to dialog request

    void onPermissionDenied(int requestCode); // user disagreed on dialog request
}
