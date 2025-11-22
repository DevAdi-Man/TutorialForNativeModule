/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import { NewAppScreen } from '@react-native/new-app-screen';
import { ScrollView, StatusBar, StyleSheet, Text, useColorScheme, View } from 'react-native';
import {
    SafeAreaProvider,
} from 'react-native-safe-area-context';

import NativeContacts from './specs/NativeContacts';
import { useEffect, useState } from 'react';
import requestContactPermission from './ContactPermission';


function App() {
    const isDarkMode = useColorScheme() === 'dark';

    return (
        <SafeAreaProvider>
            <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
            <AppContent />
        </SafeAreaProvider>
    );
}

function AppContent() {
    const [contact, setContact] = useState([{
        name: '',
        phone: ''
    }]);
    useEffect(() => {
        async function loadContacts() {
            const hasPermission = await requestContactPermission()
            if (!hasPermission) {
                console.log("Permission denied.")
                return;
            }
            try {
                const res = await NativeContacts.getContacts();
                setContact(res);
            } catch (e) {
                console.log("Error fetching contacts", e);
            }
        }

        loadContacts();
    }, []);

    console.log(contact)
    return (
        <View style={styles.container}>
            <ScrollView>
                <Text style={{ fontSize: 18, fontWeight: 'bold' }}>Contacts:</Text>

                {contact.length === 0 ? (
                    <Text>No contacts found.</Text>
                ) : (
                    contact.map((item, index) => (
                        <View key={index} style={{ marginVertical: 8 }}>
                            <Text>Name: {item.name}</Text>
                            <Text>Phone: {item.phone}</Text>
                        </View>
                    ))
                )}
            </ScrollView>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#FFF",
        padding:10
    },
});

export default App;
