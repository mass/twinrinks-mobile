//
//  MemoryManager.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/8/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "MemoryManager.h"
#import "Game.h"
#import "Team.h"

@implementation MemoryManager

-(NSArray *) getGameArray {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    // Get the results out
    NSString *results = [defaults stringForKey:@"gameData"];
    NSMutableArray *games = [[NSMutableArray alloc]init];
    NSArray *lines = [results componentsSeparatedByString:@";"];
    
    for(int i=0;i<lines.count-1;i++) {
        NSString *firstSplit = [lines objectAtIndex:i];
        Game *temp = [[Game alloc] initWithKey:firstSplit];
        if(![temp isPassed])
            [games addObject:temp];
    }
    
    NSArray *sortedGames = [games sortedArrayUsingComparator:^NSComparisonResult(Game *a, Game *b) {
        NSDate *first = [a getDateObject];
        NSDate *second = [b getDateObject];
        return [first compare:second];
    }];
    
    return sortedGames;
    //return [[NSArray alloc] initWithArray:sortedGames];
}

-(NSArray *) getTeamArray {
    NSArray *games = [self getGameArray];
    NSMutableArray *teams = [[NSMutableArray alloc] init];
    
    for(Game *game in games) {

        NSString *league = game.league;
        NSString *awayTeam = game.awayTeam;
        NSString *homeTeam = game.homeTeam;
        
        if(![homeTeam isEqualToString:@"PLAYOFF"]) {
            [teams addObject:[[Team alloc] initWithTeam:awayTeam andLeague:league]];
            [teams addObject:[[Team alloc] initWithTeam:homeTeam andLeague:league]];
        }
    }
    return [NSArray arrayWithArray:teams];
}

-(void) refreshData {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    NSError *error = nil;
    NSURL *url = [NSURL URLWithString:@"https://raw.github.com/TheMasster12/TwinRinksAdultHockey_MobileApp/6d11139e7e28b7509139dc8b1a81845a68d31cc7/ScheduleData.txt"];
    NSString *textFile = [[NSString alloc] initWithContentsOfURL:url encoding:NSUTF8StringEncoding error:&error];
    
    NSString *storedVal = textFile;
    NSString *key = @"gameData";
    [defaults setObject:storedVal forKey:key];
    [defaults synchronize];
}

@end