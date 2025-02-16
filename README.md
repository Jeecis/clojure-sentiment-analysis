# Clojure sentiment analysis
This program uses keyword based sentiment analysis to analyze text.

Test samples include: input1.txt, input2.txt, input3.txt

### Running the program

``` clj -M sentiment_analysis.clj input1.txt```

### Theoretical sentiment analysis

To analyze the sentiment, I found an existing keyword sentiment dictionary from [this github repository](https://github.com/fnielsen/afinn). See file sentiment-keywords.txt.

Each word is checked if it is included in the dictionary, if yes, then sentiment sum is updated accordingly. At the end the sum is divided by the total word count. If relative sentiment is above 0.2 it is considered to be positive, if less than -0.2 then negative. Else its neutral.