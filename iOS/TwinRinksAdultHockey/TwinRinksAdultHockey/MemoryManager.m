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
            if(![self alreadyHasTeam:teams withName:awayTeam withLeague:league])
                [teams addObject:[[Team alloc] initWithTeam:awayTeam andLeague:league]];
            if(![self alreadyHasTeam:teams withName:homeTeam withLeague:league])
                [teams addObject:[[Team alloc] initWithTeam:homeTeam andLeague:league]];
        }
    }
    return [NSArray arrayWithArray:teams];
}

-(BOOL) alreadyHasTeam:(NSMutableArray *) array withName:(NSString *)nameP withLeague:(NSString *)leagueP {
    for(int i=0;i<array.count;i++)
        if([((Team *)[array objectAtIndex:i]).teamName isEqualToString:nameP] && [((Team *)[array objectAtIndex:i]).league isEqualToString:leagueP])
            return YES;
    return NO;
}

-(NSArray *) getYourTeamArray {
    NSMutableArray *yourTeams = [[NSMutableArray alloc] init];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
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

-(void) saveYourTeamsArray:(NSArray *)array {
    NSMutableString *toSave = [[NSMutableString alloc] init];
    
    for(int i=0;i<array.count;i++) {
        toSave = [NSMutableString stringWithString:[toSave stringByAppendingString:([((Team *) [array objectAtIndex:i]) getTeamKey])]];
    }
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    NSString *data = [NSString stringWithString:toSave];
    
    [defaults setObject:data forKey:@"YourTeams"];
    [defaults synchronize];
}

-(void) refreshData {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    NSError *error = nil;
    NSURL *url = [NSURL URLWithString:@"https://raw.github.com/TheMasster12/TwinRinksAdultHockey_MobileApp/master/ScheduleData.txt"];
    NSString *textFile = [[NSString alloc] initWithContentsOfURL:url encoding:NSUTF8StringEncoding error:&error];
    
    NSString *storedVal = textFile;
    NSString *key = @"gameData";
    [defaults setObject:storedVal forKey:key];
    [defaults synchronize];
}

@end