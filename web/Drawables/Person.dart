import 'dart:html';
import 'dart:math';
import 'dart:async';
import '../Game.dart';
import '../ResourceLoader.dart';

enum People {
  Megan, Bob, Joe
}

class Person {

  ImageElement _question = new ImageElement(src:"Images/question.png");
  ImageElement _yes = new ImageElement(src:"Images/yes.png");
  ImageElement _no = new ImageElement(src:"Images/no.png");

  Random _rand = new Random();
  String _personSide = "left";
  ImageElement _personImage;
  People _selectedPerson;
  bool _dangerous;
  bool _amIChecked;
  num _ePosY = 490;
  num _ePosX = 0;
  Timer timer;

  String getPersonSide() {return _personSide;}
  //ImageElement _getPersonChoice() {return _personImage;}
  num getEPosX() {return _ePosX;}
  //num _getEPosY() {return _ePosY;}
  bool isDangerous() { return _dangerous; }

  void setPersonSide(String s) {_personSide = s;}
  void setEPosX(num n) {_ePosX = n;}

  Person(ResourceLoader resources, List<People> checkedPeople) {
    _selectedPerson = People.values[_rand.nextInt(People.values.length)];
    _amIChecked = checkedPeople.contains(_selectedPerson);

    switch(_selectedPerson) {
      case People.Megan:
        _personImage = resources.getImage("megan.png");
        _dangerous = true;
        break;
      case People.Bob:
        _personImage = resources.getImage("bob.png");
        _dangerous = false;
        break;
      case People.Joe:
        _personImage = resources.getImage("joe.png");
        _dangerous = true;
        break;
    }
    //print("new Person");
  }

  void logic(int millis) {
    if (_ePosX < 250 && _personSide == "left") {
      _ePosX += 2;
    } else if(_ePosX > 380 && _personSide == "right") {
      _ePosX -= 2;
    }
  }

  void draw(CanvasRenderingContext2D context, Game game) {
    if(game.getReady()) {
      drawPerson(context);

      if ((_personSide == "left" || _personSide == "right") /* && !game.getInfo()*/) {
        if (_amIChecked) {
          drawYes(context);
        } else {
          drawNo(context);
        }
      }
      /*  if (_personImage == _megan && !game.getInfo()) {
          if (game.getMegan()) drawYes(canvas);
          else if (!game.getMegan()) drawNo(canvas);
          else drawQuestion(canvas);
        }
        else if (_personImage == _bob && !game.getInfo()) {
          if (game.getBob()) drawYes(canvas);
          else if (!game.getBob()) drawNo(canvas);
          else drawQuestion(canvas);
        }
        else if (_personImage == _joe && !game.getInfo()) {
          if (game.getJoe()) drawYes(canvas);
          else if (!game.getJoe()) drawNo(canvas);
          else drawQuestion(canvas);
        }
      }
      else if (_personSide == "right" && !game.getChosen()) {
        if (_personImage == _megan && !game.getInfo()) {
          if (game.getMegan()) drawYes(canvas);
          else if (!game.getMegan()) drawNo(canvas);
          else drawQuestion(canvas);
        }
        else if (_personImage == _bob && !game.getInfo()) {
          if (game.getBob()) drawYes(canvas);
          else if (!game.getBob()) drawNo(canvas);
          else drawQuestion(canvas);
        }
        else if (_personImage == _joe && !game.getInfo()) {
          if (game.getJoe()) drawYes(canvas);
          else if (!game.getJoe()) drawNo(canvas);
          else drawQuestion(canvas);
        }
      }*/
    }
    //game.setReady(false);
  }

  void drawYes(CanvasRenderingContext2D context) {
    context.drawImage(_yes, _ePosX + 10, _ePosY - 50);
  }
  void drawNo(CanvasRenderingContext2D context) {
    context.drawImage(_no, _ePosX + 10, _ePosY - 50);
  }
  void drawQuestion(CanvasRenderingContext2D context) {
    context.drawImage(_question, _ePosX + 10, _ePosY - 50);
  }
  void drawPerson(CanvasRenderingContext2D context) {
    context.drawImage(_personImage, _ePosX + 10, _ePosY);
  }
}