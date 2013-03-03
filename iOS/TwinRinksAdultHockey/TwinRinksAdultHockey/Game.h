#import <Foundation/Foundation.h>

@interface Game : NSObject

@property (strong,nonatomic) NSString *homeTeam;
@property (strong,nonatomic) NSString *awayTeam;
@property (strong,nonatomic) NSString *date;
@property (strong,nonatomic) NSString *day;
@property (strong,nonatomic) NSString *rink;
@property (strong,nonatomic) NSString *startTime;
@property (strong,nonatomic) NSString *league;

-(BOOL)isPassed;
-(id)initWithKey:firstSplit;
-(NSDate *) getDateObject;

@end