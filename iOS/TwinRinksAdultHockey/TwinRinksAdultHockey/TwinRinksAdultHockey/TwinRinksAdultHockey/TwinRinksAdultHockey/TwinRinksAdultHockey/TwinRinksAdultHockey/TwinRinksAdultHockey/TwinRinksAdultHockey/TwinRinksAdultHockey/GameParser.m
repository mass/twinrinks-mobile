//
//  GameParser.m
//  TwinRinksAdultHockey
//
//  Created by AVP on 1/6/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "GameParser.h"
#import "Game.h"

@implementation GameParser

@synthesize textFile;

- (id)init {
    self = [super init];
    NSError *error = nil;
    NSURL *url = [NSURL URLWithString:@"http://www.avp42.com/zzz/bar.txt"];
    self.textFile = [[NSString alloc] initWithContentsOfURL:url encoding:NSUTF8StringEncoding error:&error];
    return self;
}

- (NSArray *) getGameList {
    NSMutableArray *games = [[NSMutableArray alloc]init];
    NSArray *lines = [self.textFile componentsSeparatedByString:@";"];
    
    for(int i=0;i<lines.count-1;i++) {
        NSArray *data = [[lines objectAtIndex:i] componentsSeparatedByString:@","];
        Game *temp = [[Game alloc] initWithArray:data];
        
        if(![temp isPassed])
            [games addObject:temp];
    }
    
    NSArray *sortedGames = [games sortedArrayUsingComparator:^NSComparisonResult(Game *a, Game *b) {
        NSDate *first = [a getDateObject];
        NSDate *second = [b getDateObject];
        return [first compare:second];
    }];
    
    NSArray *toReturn = [[NSArray alloc] initWithArray:sortedGames];
    return toReturn;
}



@end
