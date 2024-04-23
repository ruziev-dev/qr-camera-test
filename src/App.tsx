import React, {useState} from 'react';
import {Button, Image, Text, View} from 'react-native';
import {
  Callback,
  CameraOptions,
  ImagePickerResponse,
  launchCamera,
} from 'react-native-image-picker';
import {QRScannerModule, QrScannerListener} from './QrScannerListener';

const photoOptions: CameraOptions = {
  mediaType: 'photo',
  maxWidth: 1200,
  maxHeight: 1200,
  quality: 0.8,
};

const videoOptions: CameraOptions = {
  mediaType: 'video',
  videoQuality: 'high',
  durationLimit: 10,
};

const App = () => {
  const [galery, setGalery] = useState<ImagePickerResponse[]>([]);

  const addItemToGalery: Callback = response => {
    if (response.didCancel) return;
    setGalery(prev => [...prev, response]);
  };

  const onPressAddPhoto = async () => {
    // Запрос на выключение сканнера
    await QRScannerModule.turnOff();

    //Запуск камеры
    launchCamera(photoOptions, addItemToGalery);

    // Включение сканнера
    QRScannerModule.turnOn();
  };

  const onPressAddVideo = async () => {
    // Запрос на выключение сканнера
    await QRScannerModule.turnOff();

    //Запуск камеры
    launchCamera(videoOptions, addItemToGalery);

    // Включение сканнера
    QRScannerModule.turnOn();
  };

  return (
    <>
      <QrScannerListener />
      <View style={{flex: 1}}>
        <View
          style={{
            flexDirection: 'row',
            justifyContent: 'space-around',
            marginVertical: 40,
          }}>
          <Button onPress={onPressAddPhoto} title="Сделать фото" />
          <Button onPress={onPressAddVideo} title="Сделать видео" />
        </View>
        <View>
          {galery.map(item => (
            <View
              key={item.uri}
              style={{
                width: '100%',
                marginTop: 3,
                backgroundColor: 'white',
                flexDirection: 'row',
              }}>
              <Image
                style={{margin: 5}}
                source={{uri: item.uri, width: 80, height: 100}}
              />
              <Text style={{padding: 10, margin: 5, fontSize: 8}}>
                {JSON.stringify(item, null, 2)}
              </Text>
            </View>
          ))}
        </View>
      </View>
    </>
  );
};

export default App;
