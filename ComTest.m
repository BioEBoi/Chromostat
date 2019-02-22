#!/usr/bin/octave -qfw

arg_input = argv();

display("This is the input type: ")
printf(typeinfo(arg_input)

x = cell2mat(arg_input) .* 2;

disp("this is x's type: ")
print(typeinfo(x))

disp(x);

