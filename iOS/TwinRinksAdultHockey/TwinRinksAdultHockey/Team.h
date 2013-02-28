//
//  Team.h
//  TwinRinksAdultHockey
//
//  Created by Andrew on 2/28/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Team : NSObject

@property (strong,nonatomic) NSString *teamName;
@property (strong,nonatomic) NSString *league;

-(id)initWithTeam:teamNameP andLeague:leagueP;
-(NSString *)toString;
-(NSString *)getTeamKey;

@end
