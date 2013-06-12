#import <Foundation/Foundation.h>

@interface MemoryManager : NSObject

-(NSArray *) getGameArray;
-(NSArray *) getTeamArray;
-(NSArray *) getYourTeamArray;
-(void) saveYourTeamsArray:(NSArray *)array;
-(void) refreshData;

@end