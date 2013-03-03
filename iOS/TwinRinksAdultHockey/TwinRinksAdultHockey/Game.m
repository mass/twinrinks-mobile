#import "Game.h"

@implementation Game

@synthesize homeTeam,awayTeam,date,day,rink,startTime,league;

-(id)initWithKey:firstSplit {
    // 10/14/12,SU,BLU,10:40P,12:10T,Platinum,PURPLE,BROWN
    
    self = [super init];
    NSArray *data = [firstSplit componentsSeparatedByString:@","];
    
    self.date = [data objectAtIndex:0];
    self.day = [data objectAtIndex:1];
    self.rink = [data objectAtIndex:2];
    self.startTime = [NSString stringWithFormat:@"%@M",[data objectAtIndex:3]];
    self.league = [data objectAtIndex:5];
    self.homeTeam = [[data objectAtIndex:6] uppercaseString];
    self.awayTeam = [[data objectAtIndex:7] uppercaseString];
    
    return self;
}

-(id) init {
    self = [super init];
    return self;
}

-(NSDate *) getDateObject {
    NSString *timeStr = [NSString stringWithFormat:@"%@ %@",self.date,self.startTime];
    
    NSDateFormatter *format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"MM/dd/yy hh:mma"];
    NSDate *formattedDate = [format dateFromString:timeStr];
    
    return formattedDate;
}


-(BOOL)isPassed {
    NSTimeInterval now = [[NSDate date] timeIntervalSinceReferenceDate];
    NSTimeInterval test = [[self getDateObject] timeIntervalSinceReferenceDate];
    if(test - now >= -7200)
        return NO;
    return YES;
}

@end