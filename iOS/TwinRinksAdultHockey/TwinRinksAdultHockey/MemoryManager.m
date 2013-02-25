//
//  MemoryManager.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/8/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "MemoryManager.h"
#import "Game.h"

@implementation MemoryManager

-(NSArray *) getGameArray {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    // Get the results out
    NSString *results = [defaults stringForKey:@"gameData"];
    
    NSMutableArray *games = [[NSMutableArray alloc]init];
    NSArray *lines = [results componentsSeparatedByString:@";"];
    
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
    
    return [[NSArray alloc] initWithArray:sortedGames];
}

-(NSArray *) getTeamArray {
    NSArray *games = [self getGameArray];
    NSMutableSet *teams = [[NSMutableSet alloc] init];
    
    for(Game *game in games) {

        NSString *league = game.league;
        NSString *awayTeam = game.awayTeam;
        NSString *homeTeam = game.homeTeam;
        
        if(![homeTeam isEqualToString:@"PLAYOFF"]) {
            teams addObject:[[Team alloc] init:league,awayTeam];
            teams addObject:[[Team alloc] init:league,homeTeam];
        }
    }
    return [NSArray arrayWithObjects:teams count:teams.count];
}
#warning make team class you fool
#warning make the set work you fool

-(void) refreshData {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    NSError *error = nil;
    NSURL *url = [NSURL URLWithString:@"http://www.avp42.com/zzz/bar.txt"];
    NSString *textFile = [[NSString alloc] initWithContentsOfURL:url encoding:NSUTF8StringEncoding error:&error];
    
    NSString *storedVal = textFile;
    NSString *key = @"gameData";
    [defaults setObject:storedVal forKey:key];
    [defaults synchronize]; 
}

@end