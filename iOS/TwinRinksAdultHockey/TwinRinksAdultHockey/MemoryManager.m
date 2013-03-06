#import "MemoryManager.h"
#import "Game.h"
#import "Team.h"

@implementation MemoryManager

-(NSArray *) getGameArray {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *results = [defaults stringForKey:@"gameData"];
    NSArray *lines = [results componentsSeparatedByString:@";"];
    
    NSMutableArray *games = [[NSMutableArray alloc]init];
    
    if(lines.count <= 0) 
        return [[NSArray alloc]init];
    
    for(int i=0;i<lines.count-1;i++) {
        NSString *firstSplit = [lines objectAtIndex:i];
        firstSplit = [firstSplit stringByReplacingOccurrencesOfString:@" " withString:@""];
        firstSplit = [firstSplit stringByReplacingOccurrencesOfString:@"\n" withString:@""];
        
        Game *temp = [[Game alloc] initWithKey:firstSplit];
        if(![temp isPassed])
            [games addObject:temp];
    }
    return [NSArray arrayWithArray:games];
}

-(NSArray *) getTeamArray {
    NSMutableArray *teams = [[NSMutableArray alloc] init];
    
    NSArray *games = [self getGameArray];
    for(Game *game in games) {
        NSString *league = game.league;
        NSString *awayTeam = game.awayTeam;
        NSString *homeTeam = game.homeTeam;
        
        if(![self alreadyHasTeam:teams withName:awayTeam withLeague:league])
            [teams addObject:[[Team alloc] initWithTeam:awayTeam andLeague:league]];
        if(![self alreadyHasTeam:teams withName:homeTeam withLeague:league])
            [teams addObject:[[Team alloc] initWithTeam:homeTeam andLeague:league]];
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
    
    for(int i=0;i<array.count;i++) 
        toSave = [NSMutableString stringWithString:[toSave stringByAppendingString:([((Team *) [array objectAtIndex:i]) getTeamKey])]];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *data = [NSString stringWithString:toSave];
    [defaults setObject:data forKey:@"YourTeams"];
    [defaults synchronize];
}

-(void) refreshData {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    NSError *error = nil;
    NSURL *url = [NSURL URLWithString:@"https://raw.github.com/TheMasster12/twinrinks-mobile/master/ScheduleData.txt"];
    NSString *textFile = [[NSString alloc] initWithContentsOfURL:url encoding:NSUTF8StringEncoding error:&error];
    
    NSString *storedVal = textFile;
    NSString *key = @"gameData";
    [defaults setObject:storedVal forKey:key];
    [defaults synchronize];
}

@end