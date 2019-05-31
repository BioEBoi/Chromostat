#! /bin/octave -qf

clear all, clc, close all;


arg_list = argv();
arg_list_matrix = str2num(cell2mat(arg_list));

colSize = arg_list_matrix(1);

opticalDensity = arg_list_matrix(2,colSize + 1).';

matrixStart = length(arg_list_matrix) - 1 - colSize;

epValOne = arg_list_matrix(matrixStart,matrixStart + colSize).';
epValTwo = arg_list_matrix(matrixStart + colSize + 1,end).';

epValTotal = [epValOne,epValTwo];

concentrations = opticalDensity/epValTotal





