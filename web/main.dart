// Copyright (c) 2016, dash102. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.
import 'Game.dart';
import 'ResourceLoader.dart';
import 'dart:async';

void main() {
  print("Loading resources...");
  ResourceLoader resources = new ResourceLoader();
  new Timer.periodic(const Duration(milliseconds: 100), (Timer timer) {
    if(resources.isFinishedLoading()) {
      timer.cancel();
      print("Resources loaded; starting game...");
      new Game(resources);
    }
  });
}

