//
//  GameParser.h
//  TwinRinksAdultHockey
//
//  Created by AVP on 1/6/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GameParser : NSObject

@property (copy, nonatomic) NSString *textFile;
- (id) init;
- (NSArray *) getGameList;

@end
