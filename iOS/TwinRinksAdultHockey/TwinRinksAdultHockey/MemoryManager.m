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
        firstSplit = [firstSplit stringByReplacingOccurrencesOfString:@" " withString:@""];
        firstSplit = [firstSplit stringByReplacingOccurrencesOfString:@"\n" withString:@""];
        
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

-(NSArray *) getYourTeamArray {
    NSMutableArray *yourTeams = [[NSMutableArray alloc] init];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    //REMOVE THIS EVENTUALLY:
    NSString *tempData = @"Gold,BLUE;Bronze,TAN;";
    [defaults setObject:tempData forKey:@"YourTeams"];
    [defaults synchronize];
    
    NSString *teamData = [defaults objectForKey:@"YourTeams"];
    teamData = [teamData stringByReplacingOccurrencesOfString:@" " withString:@""];
    teamData = [teamData stringByReplacingOccurrencesOfString:@"\n" withString:@""];
    
    NSArray *lines = [teamData componentsSeparatedByString:@";"];
    for(int i=0;i<lines.count;i++) {
        NSArray *split = [[lines objectAtIndex:i] componentsSeparatedByString:@","];
        if(split.count == 2) {
            Team *temp = [[Team alloc] initWithTeam:[split objectAtIndex:1] andLeague:[split objectAtIndex:0]];
            [yourTeams addObject:temp];
        }
    }
    return yourTeams;
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