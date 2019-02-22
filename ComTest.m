#!/usr/bin/octave -qfw

arg_input = argv();

printf("This is the input type",typeinfo(arg_input))

x = cell2mat(arg_input) .* 2;

printf("this is x's type",typeinfo(x))

printf('%o',x);

