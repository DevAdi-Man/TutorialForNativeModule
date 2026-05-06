import { PermissionsAndroid, Platform } from "react-native";

async function requestContactPermission() {
    if (Platform.OS !== "android") return true;

    try {
        const grand = await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.READ_CONTACTS,
            {
                title: "Contacts Permission",
                message: "Allow the app to access your contacts to show who is on KurukChat.",
                buttonNeutral: "Ask Me Later",
                buttonNegative: "Cancel",
                buttonPositive: "OK"
            }
        );
        return grand === PermissionsAndroid.RESULTS.GRANTED;
    } catch (err) {
        console.warn(err)
        return false
    }

}

export default requestContactPermission
