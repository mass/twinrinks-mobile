//
//  Team.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 2/28/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "Team.h"

@implementation Team

@synthesize teamName,league;

-(id)initWithTeam:teamNameP andLeague:leagueP {
    self = [super init];
    
    self.teamName = teamNameP;
    self.league = leagueP;
    
    return self;
}

-(id) init {
    self = [super init];
    return self;
}

-(NSString *) toString {
    NSString *toReturn = [NSString stringWithFormat:@"%@ - %@",self.teamName,self.league];
    return toReturn;
}

-(NSString *) getTeamKey {
    NSString *toReturn = [NSString stringWithFormat:@"%@;%@",self.teamName,self.league];
    return toReturn;
}

@end
