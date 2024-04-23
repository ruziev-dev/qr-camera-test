import React from 'react';
import {Alert, DeviceEventEmitter, NativeModules} from 'react-native';
const QR_SCANNER_MESSAGE = 'QRscannerMessage';

export const QRScannerModule = {
  // Вызов JAVA метода turnScannerOff
  turnOff: () =>
    new Promise(res => {
      NativeModules.RNQRScanner.turnScannerOff(() =>
        console.log('NativeModules.RNQRScanner.turnScannerOff() callback'),
      );
      // установка задержки в 3сек. перед открытием камеры
      setTimeout(res, 3000);
    }),
  // Вызов JAVA метода turnScannerOn
  turnOn: () =>
    new Promise(res => NativeModules.RNQRScanner.turnScannerOn(res)),
};

export const QrScannerListener = () => {
  React.useEffect(() => {
    NativeModules.RNQRScanner.createSubscription();
    DeviceEventEmitter.addListener(QR_SCANNER_MESSAGE, event => {
      Alert.alert(QR_SCANNER_MESSAGE, event.msg);
    });

    return () => {
      NativeModules.RNQRScanner.removeSubscription();
      DeviceEventEmitter.removeAllListeners(QR_SCANNER_MESSAGE);
    };
  }, []);

  return null;
};
