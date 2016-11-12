import 'dart:html';
import 'Drawables/Person.dart';
import 'Drawables/Danger.dart';
import 'ResourceLoader.dart';
import 'dart:async';

class Game {

  CanvasElement _canvas = new CanvasElement(width: 800, height: 600);

  ImageElement _ownerImage, _houseImage, _optionsImage;

  CheckboxInputElement _meganCheck, _bobCheck, _joeCheck;
  //List<Person> _enemies = new List();
  Person activePerson;
  ButtonElement _submit;
  Element _html;
  Danger _danger;

  bool _ready = false;
  bool _info = false;

  bool getReady() {
    return _ready;
  }
  bool getInfo() {
    return _info;
  }
  void setReady(bool b) {
    _ready = b;
  }

  ResourceLoader _resources;
  List<People> _checkedPeople;

  Game(this._resources) {
    _ownerImage = _resources.getImage("owner.png");
    _houseImage = _resources.getImage("house.png");
    _optionsImage = _resources.getImage("Guest Options.png");

    _meganCheck = querySelector('#megan');
    _bobCheck = querySelector('#bob');
    _joeCheck = querySelector('#joe');
    _submit = querySelector('#viewpoints');
    _html = querySelector('#html');
    _submit.onClick.listen((_) {
      startGame();
    });
  }

  void startGame() {

    //Make a list of all the people that were checked in the beginning, if they
    //are not in this list, they are not checked.
    _checkedPeople = new List();
    if(_meganCheck.checked)
      _checkedPeople.add(People.Megan);
    if(_bobCheck.checked)
      _checkedPeople.add(People.Bob);
    if(_joeCheck.checked)
      _checkedPeople.add(People.Joe);

    _html.remove();
    _danger = new Danger();
    activePerson = new Person(_resources, _checkedPeople);

    document.body.append(_canvas);
    _canvas.tabIndex = 1;
    _canvas.focus();
    _canvas.className = "GameClass";

    _setupListeners(_canvas);

    loopDraw(); //start the drawing loop, which will draw as fast as the animation frames run

    //now start the logic loop, which changes position/functionality of the game at a fairly constant rate (60 times per second)
    Stopwatch deltaTimer = new Stopwatch();
    deltaTimer.start();
    new Timer.periodic(const Duration(microseconds: (1000000~/60)), (_) {
      int millis = deltaTimer.elapsedMilliseconds;
      deltaTimer.reset();
      logic(millis);  //logic should utilize the millis given here to determine how far things have changed
    });
  }

  /**
   * Calls itself to request animation frames for drawing constantly.
   *
   * This function returns immediately, so there is no inception/recursive
   * function calling going on here.
   */
  void loopDraw() {
    window.requestAnimationFrame((_) {
      draw(_canvas.context2D);
      loopDraw();
    });
  }

  /**
   * All the meaty math and function calling should go here.
   * NO drawing should happen here, only setup for the drawing.
   */
  void logic(int millis) {
    activePerson.logic(millis);

    _ready = true;
  }

  /**
   * Only drawing should happen here. Simple booleans and for statements (for
   * drawing) can be here.
   * NO meaty math or function calling should happen here.
   */
  void draw(CanvasRenderingContext2D context) {
    
    context.clearRect(0, 0, 800, 600);
    context.drawImage(_houseImage, 0, 0);
    //context.strokeText(Person.getDangerCount().toString(), 750, 20);
    context.font = '13pt Calibri';
    context.strokeText("Danger:", 550, 37);
    context.strokeText("Press ENTER to view character options", 10, 37);
    _danger.draw(context);

    if (_info && (activePerson.getEPosX() == 250 || activePerson.getEPosX() == 380)) {
      context.drawImage(_optionsImage, 80, 100);
    }

    context.drawImage(_ownerImage, 315, 480);
    activePerson.draw(context, this);
  }

  void _setupListeners(CanvasElement canvas) {
    _canvas.onMouseDown.listen((MouseEvent e) {
      if(_info) {
        _ready = true;
        if (e.client.x >= 175 && e.client.x <= 355 &&
            e.client.y >= 315 && e.client.y <= 395) {
          //YES
          if (activePerson.isDangerous()) {
            _danger.changeDangerBy(10); //TODO 10? 1? what?
          }
          newPerson();
        } else if (e.client.x >= 450 && e.client.x <= 625 &&
            e.client.y >= 315 && e.client.y <= 395) {
          //NO
          newPerson();
        }
      }
    });
    _canvas.onMouseMove.listen((MouseEvent e) {
      if(_info) {
        if ((e.client.x >= 175 && e.client.x <= 355 &&
            e.client.y >= 315 && e.client.y <= 395) ||
            (e.client.x >= 450 && e.client.x <= 625 &&
                e.client.y >= 315 && e.client.y <= 395)) {
          //over a button, change it to a pointer
          _canvas.style.cursor = "pointer";
        } else {
          //no longer over a button, reset pointer icon
          _canvas.style.cursor = "default";
        }
      } else {
        _canvas.style.cursor = "default";
      }
      e.preventDefault();
    });
    _canvas.onKeyDown.listen((KeyboardEvent e) {
      if(e.keyCode == KeyCode.ENTER && (activePerson.getEPosX() == 250 || activePerson.getEPosX() == 380)) {
        _info = true;
        e.preventDefault();
      }
    });
  }

  void newPerson() {
    if (/*activePerson.getEPosX() == 250 &&*/ activePerson.getPersonSide() == "left") {
      //activePerson.setEPosX(activePerson.getEPosX() + 2);
      activePerson = new Person(_resources, _checkedPeople);
      activePerson.setPersonSide("right");
      activePerson.setEPosX(800);
    } else /*(/*activePerson.getEPosX() == 380 &&*/ activePerson.getPersonSide() == "right")*/ {
      //activePerson.setEPosX(activePerson.getEPosX() - 2);
      activePerson = new Person(_resources, _checkedPeople);
      activePerson.setPersonSide("left");
      activePerson.setEPosX(0);
    }
    _info = false;
  }
}
