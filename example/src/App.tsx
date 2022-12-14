import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import openBle from 'react-native-openid4vp-ble';

const { Wallet, Verifier, MpassSmartshare } = openBle;

export default function App() {
  const [walletName, setWalletName] = React.useState<string>();
  const [verifierName, setVerifierName] = React.useState<string>();
  const [connectionName, setConnectionName] = React.useState<string>();

  React.useEffect(() => {
    Wallet.getModuleName().then((name: string) => setWalletName(name));
    Verifier.getModuleName().then((name: string) => setVerifierName(name));
    MpassSmartshare.getModuleName().then((name: string) =>
      setConnectionName(name)
    );
  }, []);

  return (
    <View style={styles.container}>
      <Text>Wallet Name: {walletName}</Text>
      <Text>Verifier Name: {verifierName}</Text>
      <Text>Text from Kotlin: {connectionName} </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
