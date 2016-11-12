import 'dart:html';

class hourglass {
  CanvasElement _canvas = new CanvasElement(width: 1048, height: 600);
  //ButtonElement _submit;
  int points = 85;

  hourglass() {
    //_submit = querySelector('#viewpoints');


    //_submit.onClick.listen((_) {
      //document.body.append(_canvas);
      document.body.insertBefore(_canvas, querySelector('buttons'));
      _canvas.tabIndex = 1;
      _canvas.focus();
      _canvas.className = "hourglass";
      showHourglass(_canvas.context2D);
    //});

  }

  void showHourglass(CanvasRenderingContext2D context) {
    loopDraw();
  }

  void loopDraw() {
    window.requestAnimationFrame((_) {
      draw(_canvas.context2D);
      loopDraw();
    });
  }

  void draw(CanvasRenderingContext2D context) {
    context.strokeStyle = '#000000';
    context.font = '18pt Comic Sans';
    context.strokeText("Hourglass", 524, 37);
    //Bottom Hourglass
    context.stroke();
    context.beginPath(); //roof
    context.moveTo(100, 200);
    context.lineTo(150, 130);
    context.lineTo(200, 200);
    context.lineTo(100, 200);
    context.closePath();

    context.strokeStyle = '#000000';
    context.stroke();
    //Bottom Fill
    context.beginPath(); //roof
    context.moveTo(100, 200);
    context.lineTo(150, 130 + points*.5);
    context.lineTo(200, 200);
    context.lineTo(100, 200);
    context.closePath();
    context.fillStyle = "#ff0000";
    context.fill();

    //Top Hourglass
    context.beginPath(); //roof
    context.moveTo(100, 60);
    context.lineTo(150, 130);
    context.lineTo(200, 60);
    context.lineTo(100, 60);
    context.closePath();




  }
}