#! /bin/octave -qf

clear all, clc, close all;

arg_list = argv();
arg_list_matrix = str2num(cell2mat(arg_list)).'
#arg_list_matrix = [3 1 2 3 4 5 6 7 8 9]

colSize = arg_list_matrix(1);

opticalDensity = arg_list_matrix(2:colSize + 1).'

matrixStart = 2 + colSize;

epValOne = arg_list_matrix(matrixStart:matrixStart + colSize - 1).'
epValTwo = arg_list_matrix(matrixStart + colSize:end).'

epValTotal = [epValOne,epValTwo]

concentrations = opticalDensity\epValTotal











