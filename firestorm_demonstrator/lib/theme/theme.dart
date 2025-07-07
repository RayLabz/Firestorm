import 'package:flutter/material.dart';

ThemeData appTheme = ThemeData(
  primarySwatch: Colors.deepOrange,

  iconButtonTheme: IconButtonThemeData(
    style: IconButton.styleFrom(
      foregroundColor: Colors.white, //Icon color
      iconSize: 30, //Icon size
    ),
  ),

  appBarTheme: AppBarTheme(
    backgroundColor: Colors.deepOrange,
    titleTextStyle: const TextStyle(
      color: Colors.white,
      fontSize: 20,
    ),
  ),

  elevatedButtonTheme: ElevatedButtonThemeData(
    style: ElevatedButton.styleFrom(
      backgroundColor: Colors.deepOrange, //Button color
      foregroundColor: Colors.white, //Text color
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10), //Padding
      textStyle: const TextStyle(fontSize: 16), //Text style
    ),
  ),

  visualDensity: VisualDensity.adaptivePlatformDensity,
);