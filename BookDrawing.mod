
set E;

set NE, dimen 2;

var x{i in E}, binary;

var y{(i,j) in NE}, binary;

s.t. desigualdad{(i,j) in NE}: x[i] - x[j] - y[i,j] >= 0;

maximize z: sum{(i,j) in NE} y[i,j];

solve;

printf "\n";
printf{(i,j) in NE} "%d %d %d\n", i, j, y[i,j];
printf "\n";
printf{i in E} "%d %d\n", i, x[i];
printf "\n";

data;

set E := 1, 2, 3, 4;

set NE := 1 2, 1 3, 3 4;

end;
