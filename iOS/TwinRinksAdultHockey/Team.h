#import <Foundation/Foundation.h>

@interface Team : NSObject

@property (strong,nonatomic) NSString *teamName;
@property (strong,nonatomic) NSString *league;

-(id)initWithTeam:teamNameP andLeague:leagueP;
-(NSString *)toString;
-(NSString *)getTeamKey;

@end
