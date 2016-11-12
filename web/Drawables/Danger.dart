import 'dart:html';

class Danger {
  static const int _TOTALDANGER = 1500;

  double _dangerWidth;
  int _dangerCount;

  Danger() {
    _dangerWidth = 0.0;
    _dangerCount = 0;
  }

  void changeDangerBy(int delta) {
    _dangerCount += delta;
  }

  void logic(int millis) {
    _dangerWidth = _dangerCount / 10;
  }

  void draw(CanvasRenderingContext2D context) {
    if(_dangerWidth < _TOTALDANGER) {
      context.strokeStyle = "gray";
      context.strokeRect(620, 20, 150, 25);
      context.fillStyle = "#F2FF00";
      context.fillRect(621, 21, _dangerWidth, 23);
    }
  }
}