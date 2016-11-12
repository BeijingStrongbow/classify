import 'dart:collection';
import 'dart:html';

class ResourceLoader {

  HashMap<String, ImageElement> _images;
  HashMap<String, AudioElement> _audio;

  List<String> _imagesRemaining, _audioRemaining;
  
  ResourceLoader() {
    List<String> imagesToLoad = new List();
    imagesToLoad.add("bob.png");
    imagesToLoad.add("Guest Options.png");
    imagesToLoad.add("house.png");
    imagesToLoad.add("joe.png");
    imagesToLoad.add("megan.png");
    imagesToLoad.add("no.png");
    imagesToLoad.add("owner.png");
    imagesToLoad.add("question.png");
    imagesToLoad.add("yes.png");

    List<String> audioToLoad = new List();
    
    _images = new HashMap();
    _audio = new HashMap();
    _loadResources(imagesToLoad, audioToLoad);
  }
  
  _loadResources(List<String> imagesToLoad, List<String> audioToLoad) {
    _imagesRemaining = imagesToLoad.toList();
    for(String image in imagesToLoad) {
      _images[image] = new ImageElement(src:"Images/" + image);
      _images[image].onLoad.listen((_) {
        _imagesRemaining.remove(image);
      });
    }

    _audioRemaining = audioToLoad.toList();
    for(String audio in audioToLoad) {
      _audio[audio] = new AudioElement("Audio/" + audio);
      _audio[audio].onCanPlay.listen((_) {
        _audioRemaining.remove(audio);
      });
      _audio[audio].preload = "auto";
      _audio[audio].load();
    }
  }

  bool isFinishedLoading() {
    return (_imagesRemaining.length == 0 && _audioRemaining.length == 0);
  }

  AudioElement getAudio(String audioName) {
    return _audio[audioName];
  }
  ImageElement getImage(String imageName) {
    return _images[imageName];
  }
}