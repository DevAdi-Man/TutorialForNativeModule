import type { TurboModule } from "react-native";
import { TurboModuleRegistry } from "react-native";

export interface Spec extends TurboModule {
    getContacts(): Promise<Array<{
        name: string;
        phone: string;
    }>>
}

export default TurboModuleRegistry.getEnforcing<Spec>(
    'NativeContacts',
)
