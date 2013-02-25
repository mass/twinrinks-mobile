//
//  MemoryManager.h
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/8/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MemoryManager : NSObject

-(NSArray *) getGameArray;
-(NSArray *) getTeamArray;
-(void) refreshData;

@end
