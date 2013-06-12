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
    return [NSString stringWithFormat:@"%@ - %@",self.league,self.teamName];
}

-(NSString *) getTeamKey {
    return [NSString stringWithFormat:@"%@,%@;",self.league,self.teamName];
}

@end