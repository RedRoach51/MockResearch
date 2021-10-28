use Spreadsheet::Read qw(ReadData);

 # Declaration of variables
 my $inputXLSX = $ARGV[0];
 my $projectSaveDirectory = $ARGV[1];
 my $startRow = $ARGV[2];
 my $endRow = $ARGV[3];

 my $book = ReadData ($inputXLSX);
 my @rows = Spreadsheet::Read::rows($book->[1]);
 
 foreach my $r ($startRow - 1 .. $endRow - 1) {
	 $colSize = scalar @{$rows[$r]};
	 $repoShortName = $rows[$r][$colSize-1];
	 # printf("Name: %s.casdf", $rows[$r][$colSize-1]);
	 system("cd $projectSaveDirectory && git clone https://github.com/apache/${repoShortName}.git");
 }
