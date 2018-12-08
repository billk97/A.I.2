This directory contains the PU1, PU2, PU3, and PUA corpora, as 
described in the paper:

I. Androutsopoulos, G. Paliouras, E. Michelakis, "Learning to 
Filter Unsolicited Commercial E-Mail", submitted for journal 
publication, 2003.

There are 4 directories (pu1, pu2, pu3, pua), each containing
one of the four corpora. 

Each one of the 4 directories in turn contains 11 subdirectories 
(part1, ..., part10, unused). These correspond to the 10 partitions 
of each corpus that were used in the 10-fold cross-validation 
experiments. In each repetition, one part was reserved for testing,
and the other 9 parts were used for training. 

Each one of the 10 subdirectories contains both spam and legitimate 
messages, one message in each file. Files whose names have the form
*spmsg*.txt are spam messages. Files whose names have the form 
*legit*.txt are legitimate messages. The first number in each filename
is random; it was used to shuffle the messages. The second number was
the initial identifier of the message; it does not reflect the order 
in which the messages were received. To bypass privacy issues, the 
messages are "encoded", as explained in the paper above. 

To maintain the same number of messages and the same spam-to-legitimate
ratio across the parts of each corpus, we had to discard some messages. 
These can be found in the "unused" directory of each corpus.

Unlike the earlier Ling-Spam corpus and the form of PU1 that was
released in 2000, the corpora in this directory are only in "bare"
form: tokens are separated by white characters, but no stop-list or
lemmatizer has been applied. Apart from this difference, the PU1 
corpus in this directory is the same as the PU1 corpus that was 
released in 2000, except that the distribution of the messages in 
the 10 parts is different, to reflect the distribution we used in the 
experiments of the paper mentioned above. Ling-Spam and the 2000 
version of PU1 are still available. 

The PU1, PU2, PU3, and PUA corpora (as well as Ling-Spam) can be 
obtained from the site of i-config:
  http://www.iit.demokritos.gr/skel/i-config/
or the publications section of Ion Androutsopoulos' web pages:
  http://www.aueb.gr/users/ion/publications.html.

You are free to use PU1, PU2, PU3, and PUA for non-commercial purposes, 
provided that you acknowledge the use and origin of the corpora in any 
published work of yours that makes use of them, and that you notify 
one of the persons below about this work. To use the four corpora
for commercial applications, you must obtain a written permission 
from the persons below.
  
Ion Androutsopoulos ( http://www.aueb.gr/users/ion/ )
George Paliouras ( http://www.iit.demokritos.gr/~paliourg/ )
Eirinaios Michelakis ( http://www.iit.demokritos.gr/~ernani/ )

This file last updated: December 16, 2003.
